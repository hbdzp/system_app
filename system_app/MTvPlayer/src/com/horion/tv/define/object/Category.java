package com.horion.tv.define.object;

public class Category extends SkyTvObject {
    private static final String DEFAULT_CATEGORY = "DEFAULT_CATEGORY_";
    private static final long serialVersionUID = -2978023554642675277L;
    public CATEGORY_ENUM category_enum = null;
    public int child_count = 0;

    public enum CATEGORY_ENUM {
        ALL,
        CCTV,
        HD,
        STAR_TV,
        OTHER,
        EXTERNAL,
        RADIO
    }

    public Category(String id, String name) {
        super(id, name);
    }

    public Category(String id, String name, CATEGORY_ENUM category_enum) {
        super(id, name);
        this.category_enum = category_enum;
    }

    public static Category create(CATEGORY_ENUM category_enum) {
        return new Category(DEFAULT_CATEGORY + category_enum.toString(), category_enum.toString(), category_enum);
    }

    protected void doAfterDeserialize() {
    }

    public String getDisplayName() {
        return this.name;
    }
}
