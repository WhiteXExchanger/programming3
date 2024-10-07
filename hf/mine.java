package hf;

import java.util.ArrayList;

class Mine {
    private VisualEnum visual;
    private int count; // Should be between 0-27

    public TypeEnum getType() {
        switch (count) {
            case 0:
                return TypeEnum.BLANK;
            case 27:
                return TypeEnum.MINE;
            default:
                return TypeEnum.COUNTER;
        }
    }
    public VisualEnum getVisual() {
        return visual;
    }
    public void setVisual(VisualEnum visual) {
        this.visual = visual;
    }
    public void setCount(int count) {
        this.count = count;
    }
}