package svenhjol.meson;

public abstract class FeatureCompat
{
    protected Feature feature;

    public FeatureCompat(Feature feature)
    {
        this.feature = feature;
    }

    public boolean hasSubscriptions()
    {
        return false;
    }

    public boolean hasTerrainSubscriptions()
    {
        return false;
    }
}
