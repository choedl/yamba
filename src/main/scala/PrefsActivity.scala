package at.yamba

import android.preference.PreferenceActivity
import android.os.Bundle

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 9/7/11
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */

class PrefsActivity extends PreferenceActivity {

  protected override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.prefs)
  }

}