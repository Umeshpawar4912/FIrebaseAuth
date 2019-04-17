package pawar.umesh.reg_log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewSignup;
    ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth auth;

    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        buttonRegister = (Button) findViewById ( R.id.buttonSignup );
        editTextEmail = (EditText) findViewById ( R.id.editTextEmail );
        editTextPassword = (EditText) findViewById ( R.id.editTextPassword );
        textViewSignup = (TextView) findViewById ( R.id.textViewSignin );
        progressDialog = new ProgressDialog ( this );

        // buttonRegister.setOnClickListener( (View.OnClickListener) this );
        // textViewSignup.setOnClickListener( (View.OnClickListener) this );

        buttonRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                final String email = editTextEmail.getText ().toString ();
                final String password = editTextPassword.getText ().toString ();

                try {
                    if (password.length () > 0 && email.length () > 0) {
                        PD.show ();
                        auth.createUserWithEmailAndPassword ( email, password )
                                .addOnCompleteListener ( MainActivity.this, new OnCompleteListener<AuthResult> () {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful ()) {
                                            Toast.makeText (
                                                    MainActivity.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG ).show ();
                                            Log.v ( "error", task.getResult ().toString () );
                                        } else {
                                            Intent intent = new Intent ( MainActivity.this, LoginActivity.class );
                                            startActivity ( intent );
                                            finish ();
                                        }
                                        PD.dismiss ();
                                    }
                                } );
                    } else {
                        Toast.makeText (
                                MainActivity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG ).show ();
                    }
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        } );
    }
}