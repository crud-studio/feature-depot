package studio.crud.feature.auth.integrations.nexmo

import com.nexmo.client.NexmoClient
import com.nexmo.client.verify.VerifyRequest
import com.nexmo.client.verify.VerifyStatus
import studio.crud.feature.auth.exception.NexmoCooldownException
import studio.crud.feature.auth.exception.NexmoGeneralException
import studio.crud.feature.auth.exception.NexmoRequestNotFoundException
import java.util.*

class NexmoClient(val apiKey: String, val apiSecret: String, val brand: String, val codeLength: NexmoCodeLength, val bypassNumbers:  Set<NexmoBypassNumberPojo> = emptySet()) {
    private val bypassNumberCache = mutableMapOf<String, NexmoBypassNumberPojo>()
    private val client = NexmoClient.Builder()
        .apiKey(apiKey)
        .apiSecret(apiSecret)
        .build()

    init {
        for (bypassNumber in bypassNumbers) {
            bypassNumberCache.put(bypassNumber.telephone, bypassNumber)
        }
    }

    private val numberRequestIds: MutableMap<String, NumberRequestDTO> = WeakHashMap()


    fun requestVerification(number: String): Long {
        val bypass = findBypassForNumber(number)
        if(bypass == null) {
            throwIfExistingRequest(number)
            val request = VerifyRequest(number, brand)
            request.pinExpiry = PIN_EXPIRY
            request.nextEventWait = NEXT_EVENT_WAIT
            request.workflow = VerifyRequest.Workflow.SMS_TTS
            request.length = codeLength.value
            val response = client.verifyClient.verify(request)
            if(response.status == VerifyStatus.ALREADY_REQUESTED) {
                // In case we have a pending request that we don't know about - add it as a new request
                numberRequestIds[number] = NumberRequestDTO(response.requestId)
                throwIfExistingRequest(number)
            }

            if(response.status != VerifyStatus.OK) {
                throw NexmoGeneralException(response.errorText)
            }

            numberRequestIds[number] = NumberRequestDTO(response.requestId)
        }
        return EFFECTIVE_COOLDOWN.toLong()
    }

    fun cancelVerification(number: String) {
        val request = getOrThrowIfNoRequest(number)
        val bypass = findBypassForNumber(number)
        if(bypass == null) {
            client.verifyClient.cancelVerification(request.requestId)
        }
        numberRequestIds.remove(number)
    }

    fun validateVerification(number: String, code: String): Boolean {
        val bypass = findBypassForNumber(number)
        if(bypass == null) {
            val request = getOrThrowIfNoRequest(number)
            val response = client.verifyClient.check(request.requestId, code)

            if(response.status == VerifyStatus.OK) {
                numberRequestIds.remove(number)
                return true
            }
        } else {
            val result =  bypass.code == code
            return result
        }
        return false
    }

    private fun getOrThrowIfNoRequest(number: String): NumberRequestDTO {
        val request = numberRequestIds[number]
        val now = System.currentTimeMillis()
        if(request == null) {
            throw NexmoRequestNotFoundException()
        } else if(now - request.timeOfRequest > EFFECTIVE_COOLDOWN * 1000) {
            numberRequestIds.remove(number)
            throw NexmoRequestNotFoundException()
        }
        return request
    }

    private fun throwIfExistingRequest(number: String) {
        val request = numberRequestIds[number]
        if(request != null) {
            val now = System.currentTimeMillis()
            if(now - request.timeOfRequest < EFFECTIVE_COOLDOWN * 1000) {
                throw NexmoCooldownException(EFFECTIVE_COOLDOWN - ((now - request.timeOfRequest) / 1000))
            } else {
                numberRequestIds.remove(number)
            }
        }
    }

    private fun findBypassForNumber(number: String): NexmoBypassNumberPojo? {
        return bypassNumberCache.get(number)
    }

    companion object {
        private const val PIN_EXPIRY = 60
        private const val NEXT_EVENT_WAIT = PIN_EXPIRY
        private const val EFFECTIVE_COOLDOWN = PIN_EXPIRY + NEXT_EVENT_WAIT
    }
}

