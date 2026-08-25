package com.example.generate;

import android.app.Activity;
import android.os.Bundle;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Calendar;

public class MainActivity extends Activity {
    int dp(float v){ return (int)(v*getResources().getDisplayMetrics().density+0.5f); }
    TextView status; EditText device, day, month, year, result;
    int blue = Color.rgb(75,98,109);

    TextView tv(String text, float size, int color){ TextView t=new TextView(this); t.setText(text); t.setTextSize(size); t.setTextColor(color); return t; }
    GradientLayout card(String title, EditText field){
        GradientLayout box=new GradientLayout(this); box.setBackgroundColor(Color.WHITE); box.setStroke(blue, dp(1));
        TextView h=tv(title,16,Color.WHITE); h.setGravity(Gravity.CENTER); h.setBackgroundColor(blue); box.addView(h,new android.widget.FrameLayout.LayoutParams(-1,dp(60)));
        field.setTextSize(21); field.setGravity(Gravity.CENTER); field.setSingleLine(true); field.setBackgroundColor(Color.TRANSPARENT);
        android.widget.FrameLayout.LayoutParams fp=new android.widget.FrameLayout.LayoutParams(-1,dp(90)); fp.topMargin=dp(60); box.addView(field,fp);
        View line=new View(this); line.setBackgroundColor(blue); android.widget.FrameLayout.LayoutParams lp=new android.widget.FrameLayout.LayoutParams(-1,dp(2)); lp.leftMargin=dp(10);lp.rightMargin=dp(10);lp.topMargin=dp(150);box.addView(line,lp);
        return box;
    }

    @Override public void onCreate(Bundle b){ super.onCreate(b);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.rgb(250,250,250));
        TextView top=tv("Generate",22,Color.WHITE); top.setGravity(Gravity.CENTER_VERTICAL); top.setPadding(dp(32),0,0,0); top.setBackgroundColor(blue); root.addView(top,new LinearLayout.LayoutParams(-1,dp(72)));
        ScrollView scroll=new ScrollView(this); LinearLayout main=new LinearLayout(this); main.setOrientation(LinearLayout.VERTICAL); main.setPadding(dp(34),dp(32),dp(34),dp(24)); scroll.addView(main); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);

        TextView logo=tv("◉  ◉\n  ▾\nDIMENSI SISTEM PRIORITY",11,Color.DKGRAY); logo.setGravity(Gravity.CENTER); main.addView(logo,new LinearLayout.LayoutParams(-1,dp(155)));

        LinearLayout dates=new LinearLayout(this); dates.setOrientation(LinearLayout.HORIZONTAL); dates.setGravity(Gravity.CENTER); main.addView(dates,new LinearLayout.LayoutParams(-1,dp(170)));
        Calendar c=Calendar.getInstance(); day=field(String.format("%02d",c.get(Calendar.DAY_OF_MONTH))); month=field(String.format("%02d",c.get(Calendar.MONTH)+1)); year=field(String.valueOf(c.get(Calendar.YEAR)));
        dates.addView(card("HARI",day),new LinearLayout.LayoutParams(0,dp(160),1)); dates.addView(space(8),new LinearLayout.LayoutParams(dp(8),1)); dates.addView(card("BULAN",month),new LinearLayout.LayoutParams(0,dp(160),1)); dates.addView(space(8),new LinearLayout.LayoutParams(dp(8),1)); dates.addView(card("TAHUN",year),new LinearLayout.LayoutParams(0,dp(160),1));

        device=field("Redmi 4A - e3a85d6e221c1d72"); device.setTextSize(17); device.setPadding(0,dp(18),0,dp(8)); main.addView(device,new LinearLayout.LayoutParams(-1,dp(65))); View divider=new View(this); divider.setBackgroundColor(Color.GRAY); main.addView(divider,new LinearLayout.LayoutParams(-1,dp(2)));

        result=field(""); result.setTextSize(17); result.setGravity(Gravity.CENTER); result.setBackgroundColor(Color.WHITE); result.setFocusable(false); result.setPadding(dp(8),dp(12),dp(8),dp(12)); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,dp(55)); rp.topMargin=dp(25); main.addView(result,rp);
        Button convert=button("CONVERT"), copy=button("COPY KEY"), reset=button("RESET"); main.addView(convert); main.addView(copy); main.addView(reset);
        status=tv("Siap.",11,Color.GRAY); status.setGravity(Gravity.CENTER); main.addView(status,new LinearLayout.LayoutParams(-1,dp(45)));
        TextView note=tv("PC/Android Edition • Masukkan tanggal dan device/key lalu tekan CONVERT.",10,Color.GRAY); note.setGravity(Gravity.CENTER); main.addView(note);

        convert.setOnClickListener(v->convert()); copy.setOnClickListener(v->copy()); reset.setOnClickListener(v->{day.setText(String.format("%02d",c.get(Calendar.DAY_OF_MONTH)));month.setText(String.format("%02d",c.get(Calendar.MONTH)+1));year.setText(String.valueOf(c.get(Calendar.YEAR)));device.setText("");result.setText("");status.setText("Siap.");});
    }
    EditText field(String s){ EditText e=new EditText(this); e.setText(s); e.setSingleLine(true); e.setGravity(Gravity.CENTER); return e; }
    View space(int w){ Space s=new Space(this); return s; }
    Button button(String s){ Button b=new Button(this); b.setText(s); b.setTextSize(16); b.setTextColor(Color.DKGRAY); b.setBackgroundColor(Color.rgb(220,222,222)); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(58)); p.topMargin=dp(8); b.setLayoutParams(p); return b; }
    void convert(){ try{ int d=Integer.parseInt(day.getText().toString()),m=Integer.parseInt(month.getText().toString()),y=Integer.parseInt(year.getText().toString()); Calendar t=Calendar.getInstance(); t.setLenient(false);t.set(y,m-1,d);t.getTime(); if(device.getText().toString().trim().isEmpty()){status.setText("Device/key belum diisi.");return;} result.setText(makeKey(device.getText().toString().trim(),d,m,y));status.setText("Key berhasil dibuat."); }catch(Exception e){status.setText("Tanggal tidak valid.");} }
    String makeKey(String dev,int d,int m,int y){ try{ String raw=dev+"|"+String.format("%02d",d)+"|"+String.format("%02d",m)+"|"+y; byte[] h=MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8)); return Base64.getUrlEncoder().withoutPadding().encodeToString(h).substring(0,24); }catch(Exception e){return "";} }
    void copy(){ if(result.getText().length()==0){status.setText("Klik CONVERT terlebih dahulu.");return;} ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);cm.setPrimaryClip(ClipData.newPlainText("Key",result.getText().toString()));status.setText("Key berhasil disalin."); }
    public static class GradientLayout extends FrameLayout { public GradientLayout(Context c){super(c);} void setStroke(int color,int w){setBackground(new android.graphics.drawable.GradientDrawable(){ {setColor(Color.WHITE);setStroke(w,color);setCornerRadius(28);} });} }
}
