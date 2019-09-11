package org.jesperancinha.chartizate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.java.Log;
import org.jesperancinha.chartizate.distributions.ChartizateDistributionAbstract;
import org.jesperancinha.chartizate.objects.ChartizateCharacterImg;
import org.jesperancinha.chartizate.objects.ChartizateCharacterImgImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log
@AllArgsConstructor
@Builder
public class ChartizateManagerImpl<C, F, B> implements ChartizateManager<C, F, B> {

    protected final ChartizateCharacterImg<C>[][] chartizateBoard;
    protected final ChartizateFontManager<F> fontManager;
    private final C background;
    private final ChartizateDistributionAbstract distribution;
    private final ChartizateEncodingManager<F> encodingManager;
    private final ChartizateImageManager<C, F, B> imageManager;
    private String destinationImagePath;

    public B generateConvertedImage() throws IOException {
        return createBufferedImage();
    }

    public B generateConvertedImageStream() throws IOException {
        return this.createBufferedImage();
    }

    public B createBufferedImage() throws IOException {
        final int imageWidth = imageManager.getImageWidth();
        int currentImageIndexX = 0;
        int rowIndex = 0;
        while (rowIndex < chartizateBoard.length) {
            List<ChartizateCharacterImgImpl<C>> chartizateRow = new ArrayList<>();
            while (currentImageIndexX < imageWidth) {
                final Character character = this.distribution.getCharacterFromArray();
                final int width = fontManager.getCharacterWidth(character);
                final int height = fontManager.getCharacterHeight(character);
                int currentImageIndexY = rowIndex * height;
                final C averageC = imageManager.getPartAverageColor(
                        currentImageIndexX,
                        currentImageIndexY,
                        currentImageIndexX + width,
                        currentImageIndexY + height
                );
                chartizateRow.add(new ChartizateCharacterImgImpl<>(averageC, this.background,
                        width, character));
                currentImageIndexX += width;
            }
            chartizateBoard[rowIndex] = chartizateRow.toArray(new ChartizateCharacterImg[0]);
            currentImageIndexX = 0;
            rowIndex++;
        }
        return imageManager.generateBufferedImage(chartizateBoard, fontManager);
    }


}