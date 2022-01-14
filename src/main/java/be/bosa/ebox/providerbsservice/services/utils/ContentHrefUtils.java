package be.bosa.ebox.providerbsservice.services.utils;

public class ContentHrefUtils {
    private ContentHrefUtils() {
    }

    public static String getContentHrefForImage(String uri, String messageProviderId, String imageId, String format) {
        return uri + String.format("/providerregistry/%s/images/%s/%s/content", messageProviderId, imageId, format);
    }

    public static String getContentHrefForMessageProvider(String uri, String messageProviderId) {
        return uri + String.format("/providerregistry/%s", messageProviderId);
    }
}
