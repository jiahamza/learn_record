package spi;

import sun.misc.Service;

import java.awt.color.ProfileDataException;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<SPIService> load = ServiceLoader.load(SPIService.class);
        Iterator<SPIService> providers = Service.providers(SPIService.class);

        Iterator<SPIService> iterator = load.iterator();
        while (iterator.hasNext()) {
            SPIService spi = iterator.next();
            spi.execute();
        }
        System.out.println("------------------------------------------------------");
        while (providers.hasNext()) {
            SPIService spi2 = providers.next();
            spi2.execute();
        }
    }
}
