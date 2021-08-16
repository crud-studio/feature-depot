package studio.crud.feature.mediafiles

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import studio.crud.feature.mediafiles.entityassociation.MediaFileEntityFieldMetadata
import studio.crud.feature.mediafiles.entityassociation.MediaFileFieldData
import studio.crud.feature.mediafiles.entityassociation.exception.IncorrectFileAclModeException
import studio.crud.feature.mediafiles.entityassociation.exception.UnauthorizedFileExtensionException
import studio.crud.feature.mediafiles.enums.MediaFileAclMode
import studio.crud.feature.mediafiles.enums.MediaFileStorageType
import studio.crud.feature.mediafiles.exception.MediaFileLocationUnavailableException
import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.storage.MediaFileStorageProvider
import studio.crud.sharedcommon.crud.jpa.model.AbstractJpaEntity
import kotlin.reflect.jvm.javaField

internal class MediaFileHandlerImplTest {
    val primaryStorageProvider: MediaFileStorageProvider = mock()

    private val mediaFileHandler = MediaFileHandlerImpl(
        primaryStorageProvider,
        mock(),
        mock()
    ).apply {
        additionalStorageProviders = emptyMap()
    }

    @Test
    fun `test validateAssociation extension is null and allowed extensions populated`() {
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "", "test", "test").apply {
            this.extension = null
        }
        val metadata = MediaFileEntityFieldMetadata(
            "test",
            "test",
            AbstractJpaEntity::class,
            "test",
            MediaFile::id.javaField!!,
            MediaFileFieldData(
                arrayOf("txt")
            )
        )
        expectThrows<UnauthorizedFileExtensionException> {
            mediaFileHandler.validateAssociation(
                mediaFile,
                metadata
            )
        }
    }

    @Test
    fun `test validateAssociation extension is not in allowed extensions`() {
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "", "test", "test").apply {
            this.extension = "jpg"
        }
        val metadata = MediaFileEntityFieldMetadata(
            "test",
            "test",
            AbstractJpaEntity::class,
            "test",
            MediaFile::id.javaField!!,
            MediaFileFieldData(
                arrayOf("txt")
            )
        )
        expectThrows<UnauthorizedFileExtensionException> {
            mediaFileHandler.validateAssociation(
                mediaFile,
                metadata
            )
        }
    }

    @Test
    fun `test validateAssociation ACL mode mismatch should throw`() {
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "", "test", "test").apply {
            this.aclMode = MediaFileAclMode.PRIVATE
        }
        val metadata = MediaFileEntityFieldMetadata(
            "test",
            "test",
            AbstractJpaEntity::class,
            "test",
            MediaFile::id.javaField!!,
            MediaFileFieldData(aclMode = MediaFileAclMode.PUBLIC)
        )
        expectThrows<IncorrectFileAclModeException> {
            mediaFileHandler.validateAssociation(
                mediaFile,
                metadata
            )
        }
    }

    @Test
    fun `test validateAssociation happy flow with extensions`() {
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "", "test", "test").apply {
            this.extension = "txt"
            this.aclMode = MediaFileAclMode.PRIVATE
        }
        val metadata = MediaFileEntityFieldMetadata(
            "test",
            "test",
            AbstractJpaEntity::class,
            "test",
            MediaFile::id.javaField!!,
            MediaFileFieldData(allowedExtensions = arrayOf("txt", "jpeg"), aclMode = MediaFileAclMode.PRIVATE)
        )
        mediaFileHandler.validateAssociation(
            mediaFile,
            metadata
        )
    }

    @Test
    fun `test validateAssociation happy flow without extensions`() {
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "", "test", "test").apply {
            this.aclMode = MediaFileAclMode.PRIVATE
        }
        val metadata = MediaFileEntityFieldMetadata(
            "test",
            "test",
            AbstractJpaEntity::class,
            "test",
            MediaFile::id.javaField!!,
            MediaFileFieldData(aclMode = MediaFileAclMode.PRIVATE)
        )
        mediaFileHandler.validateAssociation(
            mediaFile,
            metadata
        )
    }

    @Test
    internal fun `downloadFile should download if location is found`() {
        val expected = "100".toByteArray()
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "testLocation", "test", "testRemote")
        val storageProvider: MediaFileStorageProvider = mock()

        whenever(storageProvider.location).doReturn("testLocation")
        whenever(storageProvider.download("testRemote")).doReturn(expected)

        mediaFileHandler.additionalStorageProviders = mapOf(
            MediaFileStorageType.TempFile to listOf(storageProvider)
        )
        val result = mediaFileHandler.downloadFile(mediaFile)

        expectThat(result)
            .isEqualTo(expected)
    }

    @Test
    internal fun `downloadFile should throw exception if file location not found`() {
        val mediaFile = MediaFile(MediaFileStorageType.TempFile, "", "test", "test")
        expectThrows<MediaFileLocationUnavailableException> {
            mediaFileHandler.downloadFile(mediaFile)
        }
    }
}