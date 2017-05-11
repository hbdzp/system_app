package com.horion.tv.framework.ui.util;

public class EpgDataOriginDefine {

    public static class ChannellistOrigin {
        public int categoryItemIndexID = -1;

        public ChannellistOrigin(int i) {
            this.categoryItemIndexID = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ChannellistOrigin channellistOrigin = (ChannellistOrigin) obj;
            return channellistOrigin.categoryItemIndexID < 0 ? false : this.categoryItemIndexID == channellistOrigin.categoryItemIndexID;
        }

        public int hashCode() {
            return this.categoryItemIndexID;
        }
    }
}
