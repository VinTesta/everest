package com.climber.everest.model;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Evento
{
    public Integer idevento = 0;
    public String tituloevento = "";
    public String descevento = "";
    public String status = "1";
    public ArrayList<String> meses = new ArrayList<String>();
    public Character lembrete = '1';
    public String dataInicioEvento;
    public String dataFimEvento;

    public Evento() {
        meses.add("Janeiro");
        meses.add("Fevereiro");
        meses.add("Março");
        meses.add("Abril");
        meses.add("Maio");
        meses.add("Junho");
        meses.add("Julho");
        meses.add("Agosto");
        meses.add("Setembro");
        meses.add("Outubro");
        meses.add("Novembro");
        meses.add("Dezembro");
    }

    public Integer getIdevento() {
        return idevento;
    }

    public void setIdevento(Integer idevento) {
        this.idevento = idevento;
    }

    public String getTituloevento() {
        return tituloevento;
    }

    public void setTituloevento(String tituloevento) {
        this.tituloevento = tituloevento;
    }

    public String getDescevento() {
        return descevento;
    }

    public void setDescevento(String descevento) {
        this.descevento = descevento;
    }

    public String getStatusevento() {
        return status;
    }

    public void setStatusevento(String statusevento) {
        this.status = statusevento;
    }

    public Character getLembrete() {
        return lembrete;
    }

    public void setLembrete(Character lembrete) {
        this.lembrete = lembrete;
    }

    public String getDataInicioEvento() {
        return dataInicioEvento;
    }

    public void setDataInicioEvento(String dataInicioEvento) {
        this.dataInicioEvento = dataInicioEvento.substring(0, 11);
    }

    public String getDataFimEvento() {
        return dataFimEvento;
    }

    public void setDataFimEvento(String dataFimEvento) {
        this.dataFimEvento = dataFimEvento.substring(0, 11);
    }

    public void selectDateTime(View view, TextView textView, String selectData)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        final String[] dataRetorno = {""};

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String strSelectedHour = String.valueOf(selectedHour).length() == 2 ? String.valueOf(selectedHour) : "0" + String.valueOf(selectedHour);
                        String strSelectedmonth = String.valueOf(selectedmonth + 1).length() == 2 ? String.valueOf(selectedmonth + 1) : "0" + String.valueOf(selectedmonth + 1);
                        String strSelectedday = String.valueOf(selectedday).length() == 2 ? String.valueOf(selectedday) : "0" + String.valueOf(selectedday);
                        String strSelectedMinute = String.valueOf(selectedMinute).length() == 2 ? String.valueOf(selectedMinute) : "0" + String.valueOf(selectedMinute);
                        String selectedDateTime = selectedday+" de "+meses.get(selectedmonth)+" de "+selectedyear+" às "+strSelectedHour+":"+strSelectedMinute;
                        textView.setText(selectedDateTime);

                        String str = strSelectedday+"/"+strSelectedmonth+"/"+selectedyear+" "+strSelectedHour+":"+strSelectedMinute+":00";

                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = null;
                        try {
                            date = df.parse(str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long epoch = date.getTime();

                        switch (selectData)
                        {
                            case "inicio":
                                setDataInicioEvento(epoch+"");
                                break;
                            case "fim":
                                setDataFimEvento(epoch+"");
                                break;
                            default:
                                break;
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }
}
