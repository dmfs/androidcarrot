package org.dmfs.android.carrot.bindings.contentpal;

import org.dmfs.android.contentpal.RowSnapshot;

import au.com.codeka.carrot.Bindings;


/**
 * @author Marten Gajda
 */
public final class RowSnapshotBindings implements Bindings
{
    private final RowSnapshot<?> mRowSnapshot;


    public RowSnapshotBindings(RowSnapshot<?> rowSnapshot)
    {
        mRowSnapshot = rowSnapshot;
    }


    @Override
    public Object resolve(String key)
    {
        return mRowSnapshot.values().charData(key).value(null);
    }


    @Override
    public boolean isEmpty()
    {
        // assuming a row always has at least one value
        return false;
    }
}
