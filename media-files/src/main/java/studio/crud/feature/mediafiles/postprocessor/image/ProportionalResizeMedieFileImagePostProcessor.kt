package studio.crud.feature.mediafiles.postprocessor.image

import org.springframework.stereotype.Component
import studio.crud.feature.mediafiles.model.MediaFile
import java.awt.Image
import java.awt.image.BufferedImage

@Component
class ProportionalResizeMedieFileImagePostProcessor : AbstractMediaFileImagePostProcessor() {
    override fun supportedParameters(): Set<String> {
        return setOf("size")
    }

    override fun run(mediaFile: MediaFile, image: BufferedImage, parameters: Map<String, List<String>>): BufferedImage {
        val size = parameters["size"]!!.first().toInt()
        val originalWidth = image.width
        val originalHeight = image.height

        val scalingFactor = if(originalWidth > originalHeight) {
            1.0 * originalHeight / size
        } else {
            1.0 * originalWidth / size
        }

        val targetWidth = (originalWidth / scalingFactor).toInt()
        val targetHeight = (originalHeight / scalingFactor).toInt()

        val resultingImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT)
        val outputImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
        outputImage.graphics.drawImage(resultingImage, 0, 0, null)
        return outputImage
    }
}