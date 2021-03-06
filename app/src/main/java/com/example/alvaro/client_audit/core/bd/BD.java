package com.example.alvaro.client_audit.core.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alvaro.client_audit.core.exceptions.BDException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BD extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "audit.db";
	private static BD instance = null;

	private BD(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static BD getInstance(){
		if(instance == null){
			throw new BDException("Initialization required");
		}
		return instance;
	}

	public void onCreate(SQLiteDatabase bd){
		Log.d("BD", "Creating database");
		BDHelper.CREATE_TABLES(bd);
	}

	public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion){

	}

	public static void setInstanceContext(Context context) {
		instance = new BD(context);
	}
	
	public List<String[]> select(String table, String [] columns, String where,
                                 String [] whereargs, String group, String having, String orderBy){
		SQLiteDatabase bd = this.getReadableDatabase();
		List<String[]> res = new ArrayList();
		String[] fila = null;

		Cursor data = bd.query(table, columns, where, whereargs, group, having, orderBy);
		Log.d("BD", "executing query");
		if(data.moveToFirst()){
			do{
				fila = new String[data.getColumnCount()];
				for(int i = 0; i<fila.length; i++){
					fila[i] = data.getString(i);
				}
				res.add(fila);
			}while(data.moveToNext());
		}

		return res;
	}

	public void insert(String table, String numColumnHack, ContentValues values){
		this.getWritableDatabase().insert(table, numColumnHack, values);
	}
	
	public void update( String table, ContentValues values, String where, String[] whereargs){
		this.getWritableDatabase().update(table,values,where,whereargs);
	}
	
	public void delete(  String table,String where, String[] whereargs){
		this.getWritableDatabase().delete(table,where,whereargs);
	}
	
	
}
