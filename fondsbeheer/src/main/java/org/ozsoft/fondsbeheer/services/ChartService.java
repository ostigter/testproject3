package org.ozsoft.fondsbeheer.services;

import java.awt.image.BufferedImage;

import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.entities.SmallDate;

public interface ChartService {

    BufferedImage createChart(Fund fund, SmallDate from, int width, int height);

}
