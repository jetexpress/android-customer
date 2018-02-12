package coid.customer.pickupondemand.jet.model.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

public class DBQuery
{
    public static <T extends Model> int count (Class<T> c)
    {
        return new Select().from(c).count();
    }

    public static <T extends Model> T getSingle(Class<T> c)
    {
        return new Select().from(c).executeSingle();
    }
    public static <T extends Model> List<T> getAll(Class<T> c) {
        return new Select().from(c).execute();
    }

    public static <T extends Model> void deleteAll(Class<T> c)
    {
        new Delete().from(c).execute();
    }

    public static <T extends Model> void truncate(Class<T> c){
        TableInfo tableInfo = Cache.getTableInfo(c);
        ActiveAndroid.execSQL(
                String.format("DELETE FROM %s;",
                        tableInfo.getTableName()));
        ActiveAndroid.execSQL(
                String.format("DELETE FROM sqlite_sequence WHERE name='%s';",
                        tableInfo.getTableName()));
    }
}
