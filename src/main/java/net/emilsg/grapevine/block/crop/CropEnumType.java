package net.emilsg.grapevine.block.crop;

public enum CropEnumType {
    CROP_3(3, CropModelType.CROP),
    CROP_4(4, CropModelType.CROP),
    CROP_5(5, CropModelType.CROP),
    CROSS_3(3, CropModelType.CROSS),
    CROSS_4(4, CropModelType.CROSS),
    CROSS_5(5, CropModelType.CROSS),
    TALL_CROSS_3(3, CropModelType.TALL_CROSS),
    TALL_CROSS_4(4, CropModelType.TALL_CROSS),
    TALL_CROSS_5(5, CropModelType.TALL_CROSS),
    TALL_CROP_3(3, CropModelType.TALL_CROP),
    TALL_CROP_4(4, CropModelType.TALL_CROP),
    TALL_CROP_5(5, CropModelType.TALL_CROP),

    WATERLOGGED_TALL_CROSS_4(4, CropModelType.TALL_CROSS);


    private final int typeNumber;
    private final CropModelType cropModelType;


    CropEnumType(int typeNumber, CropModelType cropModelType) {
        this.typeNumber = typeNumber;
        this.cropModelType = cropModelType;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public CropModelType getCropModelType() {
        return cropModelType;
    }
}
