package org.opennms.velocloud.client;

public enum AuthTypes {
    API_KEY_AUTH("ApiKeyAuth"),
    OAUTH("OAuth"),
    BASIC_AUTH("BasicAuth");

    private final String text;

    AuthTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
