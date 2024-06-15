package svenhjol.charm.charmony.enums;

import javax.annotation.Nullable;

public enum ListType {
    TAG(10);

    private final int num;

    ListType(int num) {
        this.num = num;
    }

    @Nullable
    public ListType byNum(int num) {
        ListType[] var2 = values();
        int var3 = var2.length;

        for (ListType type : var2) {
            if (type.getNum() == num) {
                return type;
            }
        }

        return null;
    }

    public int getNum() {
        return this.num;
    }
}
