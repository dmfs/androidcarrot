/*
 * Copyright 2017 dmfs GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.android.carrot.locators;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import au.com.codeka.carrot.CarrotException;
import au.com.codeka.carrot.resource.ResourceLocater;
import au.com.codeka.carrot.resource.ResourceName;


/**
 * {@link ResourceLocater} for raw Android resources.
 * <p>
 * Note, if the given resource name is numeric, it's treated as a raw resource id, otherwise it's treated as a raw
 * resource name.
 *
 * @author Gabor Keszthelyi
 * @author Marten Gajda
 */
public final class RawResourceLocater implements ResourceLocater
{
    private final Context mAppContext;


    public RawResourceLocater(Context context)
    {
        mAppContext = context.getApplicationContext();
    }


    @Override
    public ResourceName findResource(@Nullable ResourceName parent, String name) throws CarrotException
    {
        // Raw resources don't support hierarchies, hence this is the same as #findResource(String).
        return new RawResourceName(mAppContext, name);
    }


    @Override
    public ResourceName findResource(String name) throws CarrotException
    {
        return new RawResourceName(mAppContext, name);
    }


    @Override
    public long getModifiedTime(ResourceName resourceName) throws CarrotException
    {
        return 0;
    }


    @Override
    public Reader getReader(ResourceName resourceName) throws CarrotException
    {
        InputStream inputStream = mAppContext.getResources().openRawResource(Integer.valueOf(resourceName.getName()));
        return new InputStreamReader(inputStream);
    }


    private final class RawResourceName extends ResourceName
    {
        private final Context mContext;


        RawResourceName(Context context, String name)
        {
            super(null, name);
            mContext = context;
        }


        @Override
        public String getName()
        {
            String name = super.getName();
            if (!TextUtils.isDigitsOnly(name))
            {
                // this is not an id, try to resolve the name to an id
                Resources resources = mContext.getResources();
                return String.valueOf(resources.getIdentifier(name, "raw", mContext.getPackageName()));
            }
            return name;
        }


        @Nullable
        @Override
        public ResourceName getParent()
        {
            return null;
        }
    }
}
