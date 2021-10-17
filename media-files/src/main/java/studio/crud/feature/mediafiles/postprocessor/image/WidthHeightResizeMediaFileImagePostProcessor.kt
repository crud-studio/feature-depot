package studio.crud.feature.mediafiles.postprocessor.image

import org.springframework.stereotype.Component
import studio.crud.feature.mediafiles.model.MediaFile
import java.awt.Image
import java.awt.image.BufferedImage

@Component
class WidthHeightResizeMediaFileImagePostProcessor : AbstractMediaFileImagePostProcessor() {
    override fun supportedParameters(): Set<String> {
        return setOf("w", "h")
    }

    override fun run(mediaFile: MediaFile, image: BufferedImage, parameters: Map<String, List<String>>): BufferedImage {
        val targetWidth = parameters["w"]!!.first().toInt()
        val targetHeight = parameters["h"]!!.first().toInt()

        val resultingImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT)
        val outputImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
        outputImage.graphics.drawImage(resultingImage, 0, 0, null)
        return outputImage
    }
}