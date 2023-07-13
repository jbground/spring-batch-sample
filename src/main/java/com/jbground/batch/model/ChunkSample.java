package com.jbground.batch.model;

public class ChunkSample {

    private int val;

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChunkSample{");
        sb.append("val=").append(val);
        sb.append('}');
        return sb.toString();
    }
}
