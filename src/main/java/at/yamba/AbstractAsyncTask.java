package at.yamba;

import android.os.AsyncTask;

/**
 * "Adapter" to AsyncTask to prevent runtime error
 * (see https://issues.scala-lang.org/browse/SI-1459 and
 * http://blog.nelsonsilva.eu/2009/10/31/scala-on-android-101-proguard-xmlparser-and-function2asynctask)
 *
 * TODO find better solution for this problem
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class AbstractAsyncTask<Param, Progress, Result> extends AsyncTask<Param, Progress, Result> {

    @Override
    protected Result doInBackground(Param... f) {
        return doInBackground(f[0]);
    }

    abstract protected Result doInBackground(Param f);
}
