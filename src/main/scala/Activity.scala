package at.yamba

import _root_.android.app.Activity
import android.view.View.OnClickListener
import android.view.View
import android.util.Log
import android.os.{AsyncTask, Bundle}
import winterwell.jtwitter.{TwitterException, Twitter}
import android.widget.{Toast, Button, EditText}

class StatusActivity extends Activity with OnClickListener {

  val TAG = "StatusActivity"

  var editText: EditText = null
  var updateButton: Button = null
  var twitter: Twitter = null

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.status)

    // Find views
    editText = findViewById(R.id.editText).asInstanceOf[EditText]
    updateButton = findViewById(R.id.buttonUpdate).asInstanceOf[Button]

    updateButton.setOnClickListener(this)

    twitter = new Twitter("student", "password")
    twitter.setAPIRootUrl("http://yamba.marakana.com/api")
  }

  override def onClick(v: View) {
    new PostToTwitter(sendTwitterStatusUpdate).doInBackground
    Log.d(TAG, "onClicked")
  }

  class PostToTwitter(f: () => String) {

    def doInBackground {
      new AbstractAsyncTask[() => String, Int, String] {

        protected def doInBackground(f: () => String) = {
          f()
        }

        override def onProgressUpdate(values: Int*) = {
          super.onProgressUpdate(values:_*)
        }

        override def onPostExecute(result: String) = {
          Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show
        }

      }.execute(f)
    }
  }

  private def sendTwitterStatusUpdate(): String = {
    try {
      val twitterStatus = twitter.updateStatus(editText.getText.toString)
      twitterStatus.text
    } catch {
      case e: Exception =>
        Log.e(TAG, e.toString)
        "Failed to post"
    }
  }
}
