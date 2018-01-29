package moe.plushie.dakimakuramod.common.dakimakura;

public class Daki {
    
    private final String packDirectoryName;
    private final String dakiDirectoryName;
    
    private String romajiName;
    private String originalName;
    private String author;
    private String imageFront;
    private String imageBack;
    
    public Daki(String packDirectoryName, String dakiDirectoryName) {
        this.packDirectoryName = packDirectoryName;
        this.dakiDirectoryName = dakiDirectoryName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRomajiName() {
        return romajiName;
    }

    public void setRomajiName(String romajiName) {
        this.romajiName = romajiName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    
    public String getPackDirectoryName() {
        return packDirectoryName;
    }
    
    public String getDakiDirectoryName() {
        return dakiDirectoryName;
    }
    
    public String getImageFront() {
        return imageFront;
    }
    
    public void setImageFront(String imageFront) {
        this.imageFront = imageFront;
    }
    
    public String getImageBack() {
        return imageBack;
    }
    
    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dakiDirectoryName == null) ? 0 : dakiDirectoryName.hashCode());
        result = prime * result + ((packDirectoryName == null) ? 0 : packDirectoryName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Daki other = (Daki) obj;
        if (dakiDirectoryName == null) {
            if (other.dakiDirectoryName != null)
                return false;
        } else if (!dakiDirectoryName.equals(other.dakiDirectoryName))
            return false;
        if (packDirectoryName == null) {
            if (other.packDirectoryName != null)
                return false;
        } else if (!packDirectoryName.equals(other.packDirectoryName))
            return false;
        return true;
    }
}
