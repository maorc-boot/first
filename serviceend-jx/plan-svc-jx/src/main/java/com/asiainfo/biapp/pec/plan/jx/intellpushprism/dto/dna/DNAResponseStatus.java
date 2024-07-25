package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

public enum DNAResponseStatus {
    SUCCESS("9000", "成功"),
    ;

    private final String code;
    private final String reason;

    private DNAResponseStatus(final String statusCode, final String reasonPhrase) {
        this.code = statusCode;
        this.reason = reasonPhrase;
    }

    public String getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }
}
