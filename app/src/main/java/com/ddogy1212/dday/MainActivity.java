package com.ddogy1212.dday;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int PINK = Color.rgb(255, 145, 194);
    private final int WHITE = Color.WHITE;
    private final int DARK = Color.rgb(44, 41, 58);
    private SharedPreferences pref;
    private EditText nameInput;
    private Button dateButton, pinkButton, whiteButton;
    private TextView sample;
    private LocalDate selectedDate;
    private int selectedColor;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(255, 248, 252));
        getWindow().setNavigationBarColor(Color.rgb(255, 248, 252));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        pref = getSharedPreferences("dday_settings", Context.MODE_PRIVATE);
        selectedColor = pref.getInt("color", PINK);
        String date = pref.getString("date", "");
        try { if (!date.isEmpty()) selectedDate = LocalDate.parse(date); }
        catch (RuntimeException ignored) { selectedDate = null; }

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(255, 248, 252));
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(32), dp(24), dp(32));
        scroll.addView(root);
        setContentView(scroll);

        TextView heading = text("DDAY 💕", 32, DARK, true);
        root.addView(heading);
        TextView sub = text("잠금화면에 우리만의 작은 기념일", 15, Color.rgb(112, 104, 121), false);
        LinearLayout.LayoutParams subLp = lp(-1, -2, 0, 6, 0, 26);
        root.addView(sub, subLp);

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(20), dp(22), dp(20), dp(24));
        panel.setBackground(bg(Color.WHITE, 22, Color.rgb(255, 228, 240), 1));
        root.addView(panel, lp(-1, -2, 0, 0, 0, 22));

        panel.addView(text("표시할 이름", 14, DARK, true));
        nameInput = new EditText(this);
        nameInput.setSingleLine(true);
        nameInput.setTextSize(17);
        nameInput.setTextColor(DARK);
        nameInput.setBackground(bg(Color.rgb(255, 248, 252), 12,
                Color.rgb(251, 218, 233), 1));
        nameInput.setPadding(dp(14), dp(12), dp(14), dp(12));
        nameInput.setText(pref.getString("name", "나예💕"));
        panel.addView(nameInput, lp(-1, dp(50), 0, 9, 0, 20));

        panel.addView(text("기념일", 14, DARK, true));
        dateButton = new Button(this);
        dateButton.setAllCaps(false);
        dateButton.setTextColor(DARK);
        dateButton.setTextSize(16);
        dateButton.setBackground(bg(Color.rgb(255, 248, 252), 12,
                Color.rgb(251, 218, 233), 1));
        dateButton.setOnClickListener(v -> pickDate());
        panel.addView(dateButton, lp(-1, dp(50), 0, 9, 0, 20));
        updateDateButton();

        panel.addView(text("글씨 색상", 14, DARK, true));
        LinearLayout colors = new LinearLayout(this);
        colors.setOrientation(LinearLayout.HORIZONTAL);
        pinkButton = new Button(this);
        pinkButton.setAllCaps(false);
        pinkButton.setText("💕 분홍");
        pinkButton.setTextSize(15);
        pinkButton.setOnClickListener(v -> { selectedColor = PINK; refreshSample(); });
        whiteButton = new Button(this);
        whiteButton.setAllCaps(false);
        whiteButton.setText("🤍 흰색");
        whiteButton.setTextSize(15);
        whiteButton.setOnClickListener(v -> { selectedColor = WHITE; refreshSample(); });
        colors.addView(pinkButton, new LinearLayout.LayoutParams(0, dp(48), 1));
        LinearLayout.LayoutParams whiteLp = new LinearLayout.LayoutParams(0, dp(48), 1);
        whiteLp.leftMargin = dp(8);
        colors.addView(whiteButton, whiteLp);
        panel.addView(colors, lp(-1, dp(48), 0, 10, 0, 0));

        root.addView(text("미리보기", 14, DARK, true), lp(-1, -2, 0, 0, 0, 10));
        LinearLayout preview = new LinearLayout(this);
        preview.setGravity(Gravity.CENTER);
        preview.setBackground(bg(DARK, 18, DARK, 0));
        sample = text("", 20, PINK, true);
        sample.setGravity(Gravity.CENTER);
        sample.setMaxLines(1);
        sample.setTextSize(20);
        preview.addView(sample, lp(-1, -2, 12, 0, 12, 0));
        root.addView(preview, lp(-1, dp(100), 0, 0, 0, 20));

        Button save = new Button(this);
        save.setAllCaps(false);
        save.setText("저장하고 위젯 갱신하기");
        save.setTextSize(17);
        save.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        save.setTextColor(Color.WHITE);
        save.setBackground(bg(Color.rgb(244, 99, 157), 15,
                Color.rgb(244, 99, 157), 0));
        save.setOnClickListener(v -> save());
        root.addView(save, lp(-1, dp(56), 0, 0, 0, 22));

        TextView hint = text("📍 잠금화면에 표시하는 방법\n\n1. Good Lock에서 LockStar 설치\n2. LockStar → 잠금화면 편집 → 위젯 추가\n3. '나예 D-day'를 선택하고 시계 아래 배치\n\n※ 기종과 One UI 버전에 따라 지원이 다를 수 있어요.", 14,
                Color.rgb(106, 97, 112), false);
        hint.setLineSpacing(dp(3), 1.0f);
        root.addView(hint);
        nameInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int count, int after) {}
            public void onTextChanged(CharSequence s, int st, int before, int count) { refreshSample(); }
            public void afterTextChanged(Editable s) {}
        });
        refreshSample();
        DDayWidget.refresh(this);
    }

    private void pickDate() {
        LocalDate d = selectedDate != null ? selectedDate : LocalDate.now();
        new DatePickerDialog(this, (picker, year, month, day) -> {
            selectedDate = LocalDate.of(year, month + 1, day);
            updateDateButton();
            refreshSample();
        }, d.getYear(), d.getMonthValue() - 1, d.getDayOfMonth()).show();
    }

    private void updateDateButton() {
        String date = selectedDate == null ? "날짜 선택하기" :
                selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN));
        dateButton.setText("📅  " + date);
    }

    private void refreshSample() {
        if (sample == null || nameInput == null || pinkButton == null) return;
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) name = "이름";
        String dday = "날짜 설정";
        if (selectedDate != null) {
            long n = java.time.temporal.ChronoUnit.DAYS.between(selectedDate, LocalDate.now());
            dday = n == 0 ? "D-DAY" : n > 0 ? "D+" + n : "D" + n;
        }
        sample.setText(name + " · " + dday);
        sample.setTextColor(selectedColor);
        boolean pink = selectedColor == PINK;
        pinkButton.setTextColor(pink ? Color.WHITE : DARK);
        pinkButton.setBackground(bg(pink ? Color.rgb(244, 99, 157) :
                Color.rgb(255, 248, 252), 12, Color.rgb(251, 218, 233), 1));
        whiteButton.setTextColor(pink ? DARK : Color.rgb(244, 99, 157));
        whiteButton.setBackground(bg(pink ? Color.rgb(255, 248, 252) :
                Color.rgb(251, 218, 233), 12, Color.rgb(251, 218, 233), 1));
    }

    private void save() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "이름을 입력해줘!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDate == null) {
            Toast.makeText(this, "기념일을 먼저 선택해줘!", Toast.LENGTH_SHORT).show();
            return;
        }
        pref.edit().putString("name", name).putString("date", selectedDate.toString())
                .putInt("color", selectedColor).apply();
        DDayWidget.refresh(this);
        Toast.makeText(this, "저장 완료! LockStar에서 위젯을 추가해줘 💕",
                Toast.LENGTH_LONG).show();
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(sp);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private GradientDrawable bg(int color, int radius, int borderColor, int borderWidth) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        if (borderWidth > 0) d.setStroke(dp(borderWidth), borderColor);
        return d;
    }

    private LinearLayout.LayoutParams lp(int width, int height, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, height);
        p.setMargins(dp(left), dp(top), dp(right), dp(bottom));
        return p;
    }

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
}
