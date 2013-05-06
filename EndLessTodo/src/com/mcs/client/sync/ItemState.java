package com.mcs.client.sync;

import java.util.ArrayList;

public class ItemState {
	public static final char NEW  = 'N';
	public static final char DEL  = 'D';
	public static final char UPD  = 'U';
	public static final char INVALID  = 'X';
	
	public static final String ADD_ITEM_KEY = "AddItem";
	public static final String DELETE_ITEM_KEY = "DeleteItem";
	public static final String UPDATE_ITEM_KEY = "UpdateItem";
	public static final String FILL_ITEM_KEY = "FillItem";
	
	public static final int ADD_ITEM_MSG  = 1;
	public static final int DELETE_ITEM_MSG  = 2;
	public static final int UPDATE_ITEM_MSG  = 3;
	
	public static final int FILL_ITEM_MSG = 4;	
	public static final int ALL_ITEM_MSG = 5;
	public static final int OPTIMIZED_ITEM_MSG = 6;
	
	/**²Ù×÷±êÖ¾**/
	public static boolean flag = false;
	public static final int CLEAR_CHANGElIST = 0;
	public static ArrayList<SyncItem> allItems = new ArrayList<SyncItem>();
	public static ArrayList<SyncItem> optimizedItems = new ArrayList<SyncItem>();
	public static ArrayList<SyncItem> fillItems = new ArrayList<SyncItem>();
	
	
}
