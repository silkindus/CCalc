package ccalc.silk.ccalc;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import com.google.android.gms.ads.MobileAds;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static int[] vys_arr = new int[10000];
    int digits = 8;

    //function desiged for multiplication of large numbers through String
    //conversions.
    //parameters: m, n - numbers to multiply

    public void fact(int m, int n) {

        for (int j = 0; j < vys_arr.length; j++) {
            vys_arr[j] = 0;
        }

        vys_arr[1] = 1;
        vys_arr[0] = 1;
        for (int i = m; i <= n; i++) {
            int carry = 0;
            for (int j = 1; j < vys_arr.length; j++) {
                int x = vys_arr[j] * i + carry;
                vys_arr[j] = x % 10;
                carry = x / 10;
                if (carry > 0 && j == vys_arr[0]) vys_arr[0] = j + 1;
                if (carry == 0 && j == vys_arr[0]) break;
            }
        }
    }

    //power of large numbers
    //parameters: n, k - n to k

    public void mocnina(int n, int k) {

        for (int j = 0; j < vys_arr.length; j++) {
            vys_arr[j] = 0;
        }

        vys_arr[1] = 1;
        vys_arr[0] = 1;
        for (int i = 1; i <= k; i++) {
            int carry = 0;
            for (int j = 1; j < vys_arr.length; j++) {
                int x = vys_arr[j] * n + carry;
                vys_arr[j] = x % 10;
                carry = x / 10;
                if (carry > 0 && j == vys_arr[0]) vys_arr[0] = j + 1;
                if (carry == 0 && j == vys_arr[0]) break;
            }
        }
    }

    //prints result to TextView and copies it to clipboard

    public void printvys() {
        TextView Vysledok_t = (TextView) findViewById(R.id.vysledok);
        Vysledok_t.setMovementMethod(new ScrollingMovementMethod());
        Vysledok_t.setTextIsSelectable(true);
        Vysledok_t.setText("");
        for (int y = vys_arr[0]; y > 0; y--) {
            Vysledok_t.append(String.valueOf(vys_arr[y]));   //prints all strings in the array
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("result", Vysledok_t.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    //division of array representing numbers to multiply by number k
    //paramenters: k

    public void division(int k){
        int dividend = 0, carry = 0;
        for (int i = k; i > 0; i--) {
            boolean nulovanie = true;
            for (int a = vys_arr[0]; a > 0; a--) {
                dividend = ((carry) * 10) + vys_arr[a];
                if (dividend >= i) {
                    vys_arr[a] = (int) dividend / i;
                    carry = dividend % i;
                    dividend = 0;
                    nulovanie = false;
                } else {
                    if(nulovanie) vys_arr[0] = a-1;
                    vys_arr[a] = 0;
                    carry = dividend;
                    dividend = 0;
                }
            }
        }
    }

    public long[] arrtolong() {
        int l_digits = digits;
        if(digits>vys_arr[0]) l_digits = vys_arr[0];
        long mantisa = 0;
        long exponent = vys_arr[0] - l_digits;
        for (int i = digits; i > 0; i--) {
            mantisa += (long) Math.pow((double) 10, (double) (i - 1)) * vys_arr[i + vys_arr[0] - l_digits];
        }
        //mantisa = (long) Math.pow(10,5) * vys_arr[5];
        return new long[]{mantisa, exponent, (long) l_digits};
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClickMe_v = (Button) findViewById(R.id.button_v);
        Button btnClickMe_vo = (Button) findViewById(R.id.button_vo);
        Button btnClickMe_c = (Button) findViewById(R.id.button_c);
        Button btnClickMe_co = (Button) findViewById(R.id.button_co);

        //calculations for permutations option

        btnClickMe_v.setOnClickListener(new View.OnClickListener() {
            EditText number_n;
            EditText number_k;
            TextView Vysledok_t = (TextView) findViewById(R.id.vysledok);
            TextView Vysledok1_t = (TextView) findViewById(R.id.vysledok1);

            public void onClick(View v) {
                number_n = (EditText) findViewById(R.id.number_n);
                number_k = (EditText) findViewById(R.id.number_k);
                String s_number_n = number_n.getText().toString();
                String s_number_k = number_k.getText().toString();
                if (!s_number_n.matches("") && !s_number_k.matches("")) {
                    int n = Integer.parseInt(number_n.getText().toString());
                    int k = Integer.parseInt(number_k.getText().toString());
                    fact(n-k+1, n);
                    long n_man = arrtolong()[0];
                    long n_exp = arrtolong()[1];
                    long n_dig = arrtolong()[2];

                    if (n >= k) {
                        if(n_exp == 0) Vysledok1_t.setText("" + n_man);
                        else Vysledok1_t.setText("" + n_man +" E" +n_exp);
                        printvys();
                    }
                    else {
                        Vysledok1_t.setText(getString(R.string.error));
                        Vysledok_t.setText("N/A");
                    }

                }
            }
        });

        //calculations for permutations with repetition option

        btnClickMe_vo.setOnClickListener(new View.OnClickListener() {
            EditText number_n;
            EditText number_k;
            TextView Vysledok_t = (TextView) findViewById(R.id.vysledok);
            TextView Vysledok1_t = (TextView) findViewById(R.id.vysledok1);

            public void onClick(View v) {
                number_n = (EditText) findViewById(R.id.number_n);
                number_k = (EditText) findViewById(R.id.number_k);
                String s_number_n = number_n.getText().toString();
                String s_number_k = number_k.getText().toString();
                if (!s_number_n.matches("") && !s_number_k.matches("")) {
                    int n = Integer.parseInt(number_n.getText().toString());
                    int k = Integer.parseInt(number_k.getText().toString());
                    mocnina(n,k);
                    long n_man = arrtolong()[0];
                    long n_exp = arrtolong()[1];
                    if(n_exp == 0) Vysledok1_t.setText("" + n_man);
                    else Vysledok1_t.setText("" + n_man +"E" +n_exp);
                    printvys();
                }

            }
        });

        //calculations for combinations option

        btnClickMe_c.setOnClickListener(new View.OnClickListener() {
            EditText number_n;
            EditText number_k;
            TextView Vysledok_t = (TextView) findViewById(R.id.vysledok);
            TextView Vysledok1_t = (TextView) findViewById(R.id.vysledok1);

            public void onClick(View v) {
                number_n = (EditText) findViewById(R.id.number_n);
                number_k = (EditText) findViewById(R.id.number_k);
                String s_number_n = number_n.getText().toString();
                String s_number_k = number_k.getText().toString();
                if (!s_number_n.matches("") && !s_number_k.matches("")) {
                    int n = Integer.parseInt(number_n.getText().toString());
                    int k = Integer.parseInt(number_k.getText().toString());
                    fact(n-k+1, n);
                    division(k);
                    long n_man = arrtolong()[0];
                    long n_exp = arrtolong()[1];
                    long n_dig = arrtolong()[2];
                    if (n >= k) {
                        printvys();
                        if(n_exp == 0) Vysledok1_t.setText("" + n_man);
                        else Vysledok1_t.setText("" + n_man +"E" +n_exp);
                    }
                    else {
                        Vysledok1_t.setText(getString(R.string.error));
                        Vysledok_t.setText("N/A");
                    }
                }
            }
        });

        //calculations for combinations with repetition option

        btnClickMe_co.setOnClickListener(new View.OnClickListener() {
            EditText number_n;
            EditText number_k;
            TextView Vysledok_t = (TextView) findViewById(R.id.vysledok);
            TextView Vysledok1_t = (TextView) findViewById(R.id.vysledok1);

            public void onClick(View v) {
                number_n = (EditText) findViewById(R.id.number_n);
                number_k = (EditText) findViewById(R.id.number_k);
                String s_number_n = number_n.getText().toString();
                String s_number_k = number_k.getText().toString();
                if (!s_number_n.matches("") && !s_number_k.matches("")) {
                    int n = Integer.parseInt(number_n.getText().toString());
                    int k = Integer.parseInt(number_k.getText().toString());
                    if(n < k+1) {
                        fact(n, n+k-1);
                        division(k);
                    }
                    else {
                        fact(k+1, n+k-1);
                        division(n-1);
                    }

                    long n_man = arrtolong()[0];
                    long n_exp = arrtolong()[1];
                    long n_dig = arrtolong()[2];

                    if (n > 0) {
                        printvys();
                        if(n_exp == 0) Vysledok1_t.setText("" + n_man);
                        else Vysledok1_t.setText("" + n_man +"E" +n_exp);
                    }
                    else {
                        Vysledok1_t.setText(getString(R.string.error));
                        Vysledok_t.setText("N/A");
                    }
                }
            }
        });
    }
}
