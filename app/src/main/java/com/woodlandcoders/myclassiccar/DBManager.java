package com.woodlandcoders.myclassiccar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by walt on 1/31/18.
 */

class DBManager extends SQLiteOpenHelper {

    DBManagerSchema schema;

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myClassicCar.db";

    // Empty constructor.
    DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(schema.CREATE_CAR_TABLE);
        db.execSQL(schema.CREATE_PARTS2CARS_TABLE);
        db.execSQL(schema.CREATE_PARTS_TABLE);
        db.execSQL(schema.CREATE_TPM_PARTS_TABLE);
        db.execSQL(schema.CREATE_TABLE_OIL);
        db.execSQL(schema.CREATE_TABLE_REPAIRS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // For upgrading the database if its structure changes.
    }


    // A method for finding the lowest available _id number within a db table.
    private int getLowestID(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int lowestID = 1;
        Cursor idNums = db.rawQuery("SELECT " + schema.COL_ID + " FROM " + tableName, null);
        idNums.moveToFirst();
        if (idNums.getCount() != 0) {
            do {
                if (idNums.getInt(idNums.getColumnIndex(schema.COL_ID)) == lowestID) {
                    lowestID++;
                }
                else {
                    break;
                }
            } while (idNums.moveToNext());
        }
        return lowestID;
    }

    //=================================================================
    // Car related.
    boolean addCar(ContentValues car) {
        SQLiteDatabase carsDB = this.getWritableDatabase();
        int lowestID = getLowestID(schema.TABLE_CARS);
        car.put(schema.COL_ID, lowestID);
        long result = carsDB.insert(schema.TABLE_CARS, null, car);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    Cursor getCarData() {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT * FROM " + schema.TABLE_CARS, null);
        data.moveToFirst();
        return data;
    }

    Cursor getSelectedCarInfo(int carID) {
        SQLiteDatabase carDB = this.getReadableDatabase();
        Cursor selectedCar = carDB.rawQuery("SELECT * FROM " + schema.TABLE_CARS + " WHERE " + schema.COL_ID + " = ? ", new String[]{String.valueOf(carID)});
        selectedCar.moveToFirst();
        return selectedCar;
    }

    boolean saveCarChanges(ContentValues changes, int selectedCar) {
        SQLiteDatabase carDB = this.getWritableDatabase();
        long result = carDB.update(schema.TABLE_CARS, changes, schema.COL_ID + " = ? ", new String[]{String.valueOf(selectedCar)});

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //=================================================================
    // Oil change related queries.
    Cursor getCarInfoHighestMiles(int carID) {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT MAX(" + schema.COL_MILEAGE + ") AS " + schema.COL_MILEAGE + " FROM " + schema.TABLE_OIL + " WHERE " + schema.COL_CAR_ID + " = " +  carID, null);
        data.moveToFirst();
        int x = data.getCount();
        String z = data.getString(data.getColumnIndex(schema.COL_MILEAGE));
        return data;
    }

    String getCarOilInterval(int carID){
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT " + schema.COL_OIL_INTERVAL + " FROM " + schema.TABLE_CARS + " WHERE " + schema.COL_ID + " = ?" , new String[] {String.valueOf(carID)});
        data.moveToFirst();
        String x = data.getString(data.getColumnIndex(schema.COL_OIL_INTERVAL));
        return x;
    }

    Cursor getOilInfo(int carID) {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT * FROM " + schema.TABLE_OIL + " WHERE " + schema.COL_CAR_ID + " = " + carID + " ORDER BY " + schema.COL_MILEAGE + " DESC " , null);
        data.moveToFirst();
        return data;
    }

    Cursor getLastOilChangeData(int carID) {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor tmpID = carsDB.rawQuery("SELECT MAX(" + schema.COL_ID + ") AS " + schema.COL_ID + " FROM " + schema.TABLE_OIL + " WHERE " + schema.COL_CAR_ID + " = ?" , new String[] {String.valueOf(carID)});

        tmpID.moveToFirst();
        Cursor data = carsDB.rawQuery("SELECT * FROM " + schema.TABLE_OIL + " WHERE " + schema.COL_ID + " = ?" , new String[] {String.valueOf(tmpID.getInt(tmpID.getColumnIndex(schema.COL_ID)))});
        tmpID.close();

        data.moveToFirst();
        int x = data.getCount();
        return data;
    }

    Cursor getOilChangeDataByID(int ID) {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT * FROM " + schema.TABLE_OIL + " WHERE " + schema.COL_ID + " = ?" , new String[] {String.valueOf(ID)});
        data.moveToFirst();
        int x = data.getCount();
        return data;
    }

    boolean addOilChange(ContentValues cv) {
        SQLiteDatabase latestOilChange = this.getWritableDatabase();
        long didItWork = -1;
         didItWork = latestOilChange.insert(schema.TABLE_OIL, null, cv);

         if(didItWork != -1) {
             return true;
         }
         else {
             return false;
         }
    }

    boolean updateOilChange(ContentValues changes, String oilId) {
        SQLiteDatabase anOilChange = this.getWritableDatabase();
        long didItWork = -1;
        didItWork = anOilChange.update(schema.TABLE_OIL, changes, schema.COL_ID + " = ?", new String[]{oilId});

        if(didItWork != -1) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean deleteOilChange(int id) {
        SQLiteDatabase carsDB = this.getWritableDatabase();
        int result = carsDB.delete(schema.TABLE_OIL, schema.COL_ID + " = ?", new String[]{Integer.toString(id)});

        if (result != -1) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
       boolean deleteCar(int car) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] thisCarIntString = {Integer.toString(car)};

        int result = db.delete(schema.TABLE_CARS, schema.COL_ID + " = ?", thisCarIntString);

        if (result == 1) {
            return true;
        }
        else {
            return false;
        }
    }
    */





    //=================================================================
    // Maintenance related queries.
    Cursor getMaintenanceInfo(int carID) {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT * FROM " + schema.TABLE_REPAIRS + " WHERE " + schema.COL_CAR_ID + " = ? ", new String[] {String.valueOf(carID)});
        data.moveToFirst();
        return data;
    }

    boolean addMaintenanceInfo(ContentValues cv) {
        SQLiteDatabase aMaintenanceInstance = this.getWritableDatabase();
        long didItWork = -1;
        didItWork = aMaintenanceInstance.insert(schema.TABLE_REPAIRS, null, cv);

        if(didItWork != -1) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean updateMaintenanceInfo(ContentValues changes, String repairID) {
        SQLiteDatabase aMaintenanceInstance = this.getWritableDatabase();
        long didItWork = -1;
        didItWork = aMaintenanceInstance.update(schema.TABLE_REPAIRS, changes, schema.COL_ID + " = ? ", new String[]{String.valueOf(repairID)});

        if(didItWork != -1) {
            return true;
        }
        else {
            return false;
        }
    }


    Cursor getMaintenanceInstance(int ID) {
        SQLiteDatabase carsDB = this.getReadableDatabase();
        Cursor data = carsDB.rawQuery("SELECT * FROM  " + schema.TABLE_REPAIRS + " WHERE " + schema.COL_ID + " = ? ", new String[] {String.valueOf(ID)});
        data.moveToFirst();
        int z = data.getCount();
        return data;
    }

    boolean deleteMaintenance (int id) {
        SQLiteDatabase carsDB = this.getWritableDatabase();
        int result = carsDB.delete(schema.TABLE_REPAIRS, schema.COL_ID + " = ?", new String[]{Integer.toString(id)});

        if (result != -1) {
            return true;
        }
        else {
            return false;
        }
    }


    //=================================================================
    // Parts related.
    // function is to see if there are any part available for a specific car.

    // Retrieve ====================================================================================
    Cursor getCarsToParts(int carID) {
        SQLiteDatabase carToPartsDB = this.getReadableDatabase();
        // First grabbing a list of part number to query for.
        Cursor whichPartsCursor = carToPartsDB.rawQuery("SELECT * FROM " + schema.TABLE_CARS2PARTS + " WHERE " + schema.COL_CAR_ID + " = " + carID, null);
        whichPartsCursor.moveToFirst();

        return whichPartsCursor; // I may have to do this with an ArrayList  <<-----------------------------------------------------------------------
    }

    // Used to load a ListView of parts for a specific car. This uses a INNER JOIN.
    Cursor getPartsForCars(int carID) {
        SQLiteDatabase partsDB = this.getReadableDatabase();

        // Uses a Inner Join
        String queryString = "SELECT " + schema.TABLE_PARTS + "." + schema.COL_ID + ", " + schema.TABLE_PARTS + "." + schema.COL_PHOTO + ", " + schema.TABLE_PARTS + "." + schema.COL_PART_NAME + ", " +
                schema.TABLE_PARTS + "." + schema.COL_PART_NUMBER + ", " + schema.TABLE_PARTS + "." + schema.COL_MFG + ", " + schema.TABLE_PARTS + "." + schema.COL_BARCODE + ", " +
                schema.TABLE_PARTS + "." + schema.COL_NOTES +
                " FROM " + schema.TABLE_PARTS +
                " INNER JOIN " + schema.TABLE_CARS2PARTS +
                " WHERE " + schema.TABLE_CARS2PARTS + "." + schema.COL_PART_ID + " = " + schema.TABLE_PARTS + "." + schema.COL_ID + " AND " + schema.TABLE_CARS2PARTS + "." + schema.COL_CAR_ID + " = " + carID +
                " ORDER BY " + schema.TABLE_PARTS + "." + schema.COL_PART_NAME + " ASC";

        Cursor partsList = partsDB.rawQuery(queryString , null);
        partsList.moveToFirst();
        return partsList;
    }

    Cursor getPart(int part) {
        SQLiteDatabase getPartDB = this.getReadableDatabase();
        String queryString = "select * from " + schema.TABLE_PARTS + " where " + schema.COL_ID + " = " + part;
        Cursor thePart = getPartDB.rawQuery(queryString, null);
        thePart.moveToFirst();
        return thePart;
    }

    Cursor getTpmParts(int partNum) {
        SQLiteDatabase getTPMdb = this.getReadableDatabase();
        String q = "select * from " + schema.TABLE_TPM_PARTS + " where "  + schema.COL_PART_ID + " = " + partNum;
        Cursor tpmParts = getTPMdb.rawQuery(q, null);
        tpmParts.moveToFirst();
        return tpmParts;
    }


    Cursor getSingleTpmPart(int partNum) {
        SQLiteDatabase getTPMdb = this.getReadableDatabase();
        String queryString = "select * from " + schema.TABLE_TPM_PARTS + " where "  + schema.COL_ID + " = " + partNum;
        Cursor tpmParts = getTPMdb.rawQuery(queryString, null);
        tpmParts.moveToFirst();
        return tpmParts;
    }

    Cursor getAdjustedOemParts(int carID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT " + schema.TABLE_PARTS + "." + schema.COL_ID + " AS " + schema.COL_ID + ", " +
                schema.TABLE_PARTS + "." + schema.COL_PART_NAME + " AS " + schema.COL_PART_NAME+ ", " +
                schema.TABLE_PARTS + "." + schema.COL_PART_NUMBER + " AS " + schema.COL_PART_NUMBER + ", " +
                schema.TABLE_PARTS + "." + schema.COL_MFG + " AS " + schema.COL_MFG + ", " +
                schema.TABLE_PARTS + "." + schema.COL_BARCODE + " AS " + schema.COL_BARCODE + ", " +
                schema.TABLE_PARTS + "." + schema.COL_PHOTO + " AS " +schema.COL_PHOTO +  ", " +
                schema.TABLE_PARTS + "." + schema.COL_NOTES + " AS " + schema.COL_NOTES +
                " FROM " + schema.TABLE_PARTS +
                " EXCEPT " +
                "SELECT " + schema.TABLE_PARTS + "." + schema.COL_ID + " AS " + schema.COL_ID + ", " +
                schema.TABLE_PARTS + "." + schema.COL_PART_NAME + " AS " + schema.COL_PART_NAME+ ", " +
                schema.TABLE_PARTS + "." + schema.COL_PART_NUMBER + " AS " + schema.COL_PART_NUMBER + ", " +
                schema.TABLE_PARTS + "." + schema.COL_MFG + " AS " + schema.COL_MFG + ", " +
                schema.TABLE_PARTS + "." + schema.COL_BARCODE + " AS " + schema.COL_BARCODE + ", " +
                schema.TABLE_PARTS + "." + schema.COL_PHOTO + " AS " +schema.COL_PHOTO +  ", " +
                schema.TABLE_PARTS + "." + schema.COL_NOTES + " AS " + schema.COL_NOTES +
                        " FROM " + schema.TABLE_PARTS +
                        " INNER JOIN " + schema.TABLE_CARS2PARTS +
                        " WHERE " + schema.TABLE_CARS2PARTS + "." + schema.COL_PART_ID + " = " + schema.TABLE_PARTS + "." + schema.COL_ID + " AND " + schema.TABLE_CARS2PARTS + "." + schema.COL_CAR_ID + " = " + carID +
                        " ORDER BY " + schema.TABLE_PARTS + "." + schema.COL_PART_NAME + " ASC";
        Cursor allOemPartsCurcor = db.rawQuery(queryString, null);
        allOemPartsCurcor.moveToFirst();

        return allOemPartsCurcor;
    }

    // Add =========================================================================================
    boolean addPart(ContentValues part, int carID) {
        SQLiteDatabase savePart = this.getWritableDatabase();
        long result = -1;

        // Getting the lowest _id number value in the parts
        // table and setting it as the _id for our new part.
        int lowestID = getLowestID(schema.TABLE_PARTS);
        int partID = lowestID;
        part.put(schema.COL_ID, lowestID);

        // Inserting part into db.
        savePart.insert(schema.TABLE_PARTS, null, part);

        // Setting up mapping for the CarsToParts table
        ContentValues loadCarsToParts = new ContentValues();

        lowestID = getLowestID(schema.TABLE_CARS2PARTS);
        loadCarsToParts.put(schema.COL_ID, lowestID);
        loadCarsToParts.put("PartID", partID);
        loadCarsToParts.put("CarID", carID);

        // Setting value in Cars2Parts table
        result = savePart.insert(schema.TABLE_CARS2PARTS, null, loadCarsToParts);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    boolean addTpmPart(ContentValues tpmPart) {
        SQLiteDatabase addTPMdb = this.getWritableDatabase();
        int lowestID = getLowestID(schema.TABLE_TPM_PARTS);
        tpmPart.put(schema.COL_ID, lowestID);
        long result = addTPMdb.insert(schema.TABLE_TPM_PARTS, null, tpmPart);

        if (result != -1) {
            return true;
        }
        else {
            return false;
        }
    }


    void selectExistingPart(ContentValues cv) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "insert into " + schema.TABLE_CARS2PARTS + " (" + schema.COL_CAR_ID + ", " + schema.COL_PART_ID + ") values(" + cv.get("carID") + ", " + cv.get("partID") + ")";
        db.insert(schema.TABLE_CARS2PARTS, null, cv);
    }


    // Update ======================================================================================
    boolean updateTpmPart(ContentValues changes, int tpmPartId) {
        SQLiteDatabase updateTpmPart = this.getWritableDatabase();
        String q = "update " + schema.TABLE_TPM_PARTS + " where " + schema.COL_ID + " = " + tpmPartId;
        long result = updateTpmPart.update(schema.TABLE_TPM_PARTS, changes, schema.COL_ID + " = ?", new String[]{Integer.toString(tpmPartId)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    boolean updatePart(ContentValues changes, int partId) {
        SQLiteDatabase updatePart = this.getWritableDatabase();
        long result = updatePart.update(schema.TABLE_PARTS, changes, schema.COL_ID + " = ?", new String[]{Integer.toString(partId)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
   }



    // Delete ======================================================================================

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Deleting a car is performed in the CarInfoActivity.java near the bottom of the file
    // It's performed in multiple pieces with the functions found here.
    ///////////////////////////////////////////////////////////////////////////////////////////////


    boolean deleteTPMPart(int partID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] i = new String[]{Integer.toString(partID)};
        int result = db.delete(schema.TABLE_TPM_PARTS, schema.COL_ID + " = ?", new String[]{Integer.toString(partID)});

        if (result > 1) {
            return  false;
        }
        else {
            return true;
        }
    }

    // I need to do a query of the CarsToParts table to see if a certain a part is being shared by 2 or more cars
    // and then only remove the one entry for a specific car, or perform the above operation in total. I will most likely
    // need an if/else statement.
    boolean deleteOemPart(int partID, int carID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int aResults;
        int bResults = -1;
        int cResults = -1;

        String[] thisPartAndCarIntString = {Integer.toString(partID), Integer.toString(carID)};

        Cursor c2p = db.rawQuery("SELECT * FROM " + schema.TABLE_CARS2PARTS + " WHERE " + schema.COL_PART_ID + " = ?", new String[]{Integer.toString(partID)});
        DatabaseUtils.dumpCursor(c2p);
        c2p.moveToFirst();

        // We have to delete the TPM parts first before deleting the OEM part.
        aResults = db.delete(schema.TABLE_CARS2PARTS, schema.COL_PART_ID + " = ? AND " + schema.COL_CAR_ID + " = ? ", thisPartAndCarIntString);

        if (c2p.getCount() == 1) {
            bResults = db.delete(schema.TABLE_TPM_PARTS, schema.COL_PART_ID + " = ? ",new String[]{Integer.toString(partID)});
            cResults = db.delete(schema.TABLE_PARTS, schema.COL_ID + " = ? ", new String[]{Integer.toString(partID)});
        }

        if ((aResults > 0) | (bResults > 0) | (cResults > 0)) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean deleteCar(int car) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(schema.TABLE_CARS, schema.COL_ID + " = ?", new String[]{Integer.toString(car)});

        if (result == 1) {
            return true;
        }
        else {
            return false;
        }
    }

}
