package com.zone.entity;

/**
 * @ClassName ThirdWord
 * @Author zone
 * @Date 2018/12/31  14:14
 * @Version 1.0
 * @Description  调用三方接口翻译的实体对象
 */
public class ThirdWord {
    //tranlate source
    private String src;

    //transla result
    private String tgt;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    @Override
    public String toString() {
        return "ThirdWord{" +
                "\n\t src='" + src + '\'' +
                ",\n\t tgt='" + tgt + '\'' +
                '}';
    }
}
