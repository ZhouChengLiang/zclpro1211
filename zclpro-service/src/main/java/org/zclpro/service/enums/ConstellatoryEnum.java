package org.zclpro.service.enums;

import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

public enum ConstellatoryEnum {
	ARIES(1,"白羊座"),
	TAURUS(2,"金牛座"),
	GEMINI(3,"双子座"),
	CANCER(4,"巨蟹座"),
	LEO(5,"狮子座"),
	VIRGO(6,"处女座"),
	LIBRA(7,"天秤座"),
	SCORPIO(8,"天蝎座"),
	SAGITTARIUS(9,"射手座"),
	CAPRICORN(10,"摩羯座"),
	AQUARIUS(11,"水瓶座"),
	PISCES(12,"双鱼座");
	
	@Getter
    private Integer code;
	
	@Getter
    private String name;
	
	private ConstellatoryEnum(Integer code,String name){
		this.code = code;
		this.name = name;
	}
	
	private static final ImmutableMap<Integer, ConstellatoryEnum> codeMap;

    static {
        ImmutableMap.Builder<Integer, ConstellatoryEnum> builder = ImmutableMap.builder();
        for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
            builder.put(constellatoryEnum.getCode(), constellatoryEnum);
        }
        codeMap = builder.build();
    }

    private static final ImmutableMap<String, ConstellatoryEnum> nameMap;

    static {
        ImmutableMap.Builder<String, ConstellatoryEnum> builder = ImmutableMap.builder();
        for (ConstellatoryEnum constellatoryEnum : ConstellatoryEnum.values()) {
            builder.put(constellatoryEnum.getName(), constellatoryEnum);
        }
        nameMap = builder.build();
    }

    /**
     * 通过名称获取县枚举对象
     *
     * @param name
     * @return
     */
    public static ConstellatoryEnum nameOf(String name) {
        return Optional.of(nameMap.get(name)).get();
    }


    /**
     * 通过code获取对应的ConstellatoryEnum
     *
     * @param ename
     * @return
     */
    public static ConstellatoryEnum codeOf(int code) {
        return Optional.of(codeMap.get(code)).get();
    }
}
