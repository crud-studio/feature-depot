package studio.crud.feature.mediafiles.postprocessor.image

import studio.crud.feature.mediafiles.model.MediaFile
import studio.crud.feature.mediafiles.postprocessor.MediaFilePostProcessor
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

abstract class AbstractMediaFileImagePostProcessor : MediaFilePostProcessor {
    override fun supportedExtensions(): Set<String> {
        return setOf("bmp", "jpeg", "jpg", "png")
    }

    final override fun run(mediaFile: MediaFile, bytes: ByteArray, parameters: Map<String, List<String>>): ByteArray {
        val inputStream = ByteArrayInputStream(bytes)
        val image = ImageIO.read(inputStream)
        val resultImage = run(mediaFile, image, parameters)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(resultImage, mediaFile.extension, outputStream)
        return outputStream.toByteArray()
    }

    protected abstract fun run(mediaFile: MediaFile, image: BufferedImage, parameters: Map<String, List<String>>): BufferedImage
}