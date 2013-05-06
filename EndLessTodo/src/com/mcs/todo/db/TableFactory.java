package com.mcs.todo.db;

public class TableFactory {
	
	public static DBTable getTable(String name) {
		if(name.equals(DBConstant.TABLE_TODOLIST)) {
			return new Todolist();
		}
		else 
		{
			if (name.equals(DBConstant.TABLE_WIDGET_SETTING))
				return new WidgetSetting();
			else 
			{
				if(name.equals(DBConstant.TABLE_NOTELIST))
				{
					return new Notelist();
				}
				else
				{
					if(name.equals(DBConstant.TABLE_NOTECATEGORY))
					{
						return new Notecategory();
					}
					else{
						if(name.equals(DBConstant.TABLE_TODOLIST_CHANGELIST))
						{
							return new ChangeList();
						}
					}
				}
			}
		}		
		return null;
	}
}
