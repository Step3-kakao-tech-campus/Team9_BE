package com.kakao.linknamu._core.encryption;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EncryptionAlgorithm {
    AES("AES/GCM/NoPadding", "AES", 12, 16 * 8);

    private final String algorithm;
    private final String mainAlgorithm;
    private final int ivByteLength;
    private final int tagBitLength;


}
