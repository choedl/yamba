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


// Another Solution:


/**
 * Temporary workaround to solve a Scala compiler issue which shows up
 * at runtime with the error message
 * "java.lang.AbstractMethodError: abstract method not implemented"
 * for the missing method LookupTask.doInBackground(String... args).
 *
 * Our solution: the Java method doInBackground(String... args) forwards
 * the call to the Scala method doInBackground1(String[] args).
 */
/*public abstract class MyAsyncTask extends AsyncTask<String, String, String> {

    protected abstract String doInBackground1(String[] args);

    @Override
    protected String doInBackground(String... args) {
        String[] args1 = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            args1[i] = args[i];
        }
        return doInBackground1(args1);
    }

}*/