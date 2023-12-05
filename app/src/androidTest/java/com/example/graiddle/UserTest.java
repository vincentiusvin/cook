package com.example.graiddle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.graiddle.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserTest {
    private String EMAIL = "usertest@mail.com";
    private String USERNAME = "Test User";
    private String PASSWORD = "123456";

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Test
    public void run() throws Exception{
        AuthResult register = Tasks.await(User.register(USERNAME, EMAIL, PASSWORD));
        AuthResult login = Tasks.await(auth.signInWithEmailAndPassword(EMAIL, PASSWORD));
        assertEquals(register.getUser().getEmail(), EMAIL);
        assertEquals(login.getUser().getEmail(), EMAIL);

        String uid = login.getUser().getUid();
        User user = new User(uid);

        DocumentSnapshot doc = Tasks.await(user.getReference().get());
        assertTrue(doc.exists());

        Tasks.await(user.getReference().delete());
        Tasks.await(login.getUser().delete());
    }
}
