package org.dmfs.android.carrot.bindings.contentpal;

import org.dmfs.android.contentpal.RowSet;
import org.dmfs.android.contentpal.RowSnapshot;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Function;

import java.util.Iterator;

import au.com.codeka.carrot.Bindings;


/**
 * An {@link Iterable} which binds all {@link RowSnapshot} of a {@link RowSet} as they are iterated.
 *
 * @author Marten Gajda
 */
public final class Bound<T> implements Iterable<Bindings>
{
    private final Iterable<Bindings> mDelegate;


    public Bound(RowSet<T> rowSet)
    {
        this(rowSet, new Function<RowSnapshot<T>, Bindings>()
        {
            @Override
            public Bindings apply(RowSnapshot<T> argument)
            {
                return new RowSnapshotBindings(argument);
            }
        });
    }


    public Bound(RowSet<T> rowSet, Function<RowSnapshot<T>, Bindings> bindFunction)
    {
        mDelegate = new Mapped<>(rowSet, bindFunction);
    }


    @Override
    public Iterator<Bindings> iterator()
    {
        return mDelegate.iterator();
    }
}
