package moe.plushie.dakimakuramod.common.dakimakura.pack;

public class DakiPackClient extends AbstractDakiPack {

    public DakiPackClient(String name) {
        super(name);
    }
    
    @Override
    public String getName() {
        if (getResourceName().endsWith(".zip")) {
            return getResourceName().substring(0, getResourceName().length() - 4);
        }
        return getResourceName();
    }
    
    @Override
    public byte[] getResource(String path) {
        return null;
    }
    
    @Override
    public boolean resourceExists(String path) {
        return false;
    }
}
