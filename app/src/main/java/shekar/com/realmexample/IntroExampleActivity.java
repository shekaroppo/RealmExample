
package shekar.com.realmexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.Realm;
import shekar.com.realmexample.model.Dog;
import shekar.com.realmexample.model.Person;


public class IntroExampleActivity extends Activity {

    public static final String TAG = IntroExampleActivity.class.getName();
    private LinearLayout rootLayout = null;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_basic_example);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        // These operations are small enough that
        // we can generally safely run them on the UI thread.

        // Open the default Realm for the UI thread.
        realm = Realm.getInstance(this);

        basicCRUD(realm);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        rootLayout.addView(tv);
    }

    private void basicCRUD(Realm realm) {
        showStatus("Perform basic Create/Read/Update/Delete (CRUD) operations...");

        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();

        // Add a person
        Person person = realm.createObject(Person.class);
        person.setId(1);
        person.setName("Young Person");
        person.setAge(14);

        // When the transaction is committed, all changes a synced to disk.
        realm.commitTransaction();

        // Find the first person (no query conditions) and read a field
        person = realm.where(Person.class).findFirst();
        showStatus(person.getName() + ":" + person.getAge());

        // Update person in a transaction
        realm.beginTransaction();
        Dog fido = realm.createObject(Dog.class);
        fido.setName("Thunder");
        person.setName("Senior Person");
        person.setAge(99);
        person.setDog(fido);
        showStatus(person.getName() + " got older: " + person.getAge());
        realm.commitTransaction();

//        // Delete all persons
//        realm.beginTransaction();
//        realm.allObjects(Person.class).clear();
//        realm.commitTransaction();
    }


}
