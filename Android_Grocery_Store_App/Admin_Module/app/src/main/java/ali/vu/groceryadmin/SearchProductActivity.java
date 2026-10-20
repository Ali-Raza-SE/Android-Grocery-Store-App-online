package ali.vu.groceryadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchProductActivity extends AppCompatActivity {

    private Button btnSearchProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        btnSearchProducts = findViewById(R.id.btnSearchProducts);
        btnSearchProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchProductActivity.this, "Product not found!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}