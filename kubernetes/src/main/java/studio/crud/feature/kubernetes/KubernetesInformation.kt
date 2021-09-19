package studio.crud.feature.kubernetes

import org.springframework.stereotype.Component

@Component
class KubernetesInformation {
    /**
     * The URL of the service if found via env `CSVC_INGRESS_URL`
     */
    val url: String? = System.getenv("CSVC_INGRESS_URL")
}