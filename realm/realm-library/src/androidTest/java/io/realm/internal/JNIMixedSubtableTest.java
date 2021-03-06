/*
 * Copyright 2015 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.internal;

import junit.framework.TestCase;

import java.util.Date;

import io.realm.RealmFieldType;

public class JNIMixedSubtableTest extends TestCase {

    public void testGetSubtableFromMixedColumnTest() {
        Util.setDebugLevel(2);
        Table table = new Table();

        table.addColumn(RealmFieldType.INTEGER, "num");
        table.addColumn(RealmFieldType.UNSUPPORTED_MIXED, "mix");

        // No rows added yet
        try { Table subtable = table.getSubtable(1, 0); fail("No rows added, index out of bounds"); } catch (ArrayIndexOutOfBoundsException e) { }

        // addEmptyRow() will put Mixed(0) as default value into the mixed column.
        table.addEmptyRow();
        // Getting a subtable on a mixed with a 0 int value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains an int, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a binary
        table.setMixed(1, 0, new Mixed(new byte[] {1,2,3}));
        // Getting a subtable on a mixed with a date value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains an binary, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a bool
        table.setMixed(1, 0, new Mixed(true));
        // Getting a subtable on a mixed with a String value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains a bool, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a date
        table.setMixed(1, 0, new Mixed(new Date()));
        // Getting a subtable on a mixed with a date value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains a date, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a double
        table.setMixed(1, 0, new Mixed(3.0d));
        // Getting a subtable on a mixed with a date value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains a double, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a float
        table.setMixed(1, 0, new Mixed(3.0f));
        // Getting a subtable on a mixed with a date value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains a float, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a int
        table.setMixed(1, 0, new Mixed(300));
        // Getting a subtable on a mixed with a date value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains an int, not a subtable"); } catch (IllegalArgumentException e) { }

        // Now we set the Mixed value to a String
        table.setMixed(1, 0, new Mixed("s"));
        // Getting a subtable on a mixed with a String value should not work
        try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains a String, not a subtable"); } catch (IllegalArgumentException e) { }

        /* FIXME: Subtable in Mixed is currently not supported
        // Now we specifically set the Mixed value to a subtable
        table.setMixed(1, 0, new Mixed(RealmFieldType.UNSUPPORTED_TABLE));
        // Getting a subtable on the mixed column is now allowed
        Table subtable = table.getSubtable(1, 0);
        */
    }

    // Test uses TableSpec..
    public void testShouldCreateSubtableInMixedTypeColumn() {
        Table table = new Table();

        TableSpec tableSpec = new TableSpec();
        tableSpec.addColumn(RealmFieldType.INTEGER, "num");
        tableSpec.addColumn(RealmFieldType.UNSUPPORTED_MIXED, "mix");
        TableSpec subspec = tableSpec.addSubtableColumn("subtable");
        subspec.addColumn(RealmFieldType.INTEGER, "num");
        table.updateFromSpec(tableSpec);

        // Shouldn't work: no Mixed stored yet
        //Mixed m1 = table.getMixed(1, 0);
        //ColumnType mt = table.getMixedType(1,0);

        // You can't "getSubtable()" unless there is one. And the addEmptyRow will put in a Mixed(0) as default.
        // You now get an exception instead of crash if you try anyway
        {
            table.addEmptyRow();

            try { Table subtable = table.getSubtable(1, 0); fail("Mixed contains 0, not a subtable");  } catch (IllegalArgumentException e) {}
            table.removeLast();
        }

        /* FIXME: Subtable in Mixed is currently not supported
        long ROW = 0;
        // Add empty row - the simple way
        table.addEmptyRow();
        table.setMixed(1, ROW, new Mixed(RealmFieldType.UNSUPPORTED_TABLE));
        assertEquals(1, table.size());
        assertEquals(0, table.getSubtableSize(1, 0));

        // Create schema for the one Mixed cell with a subtable
        Table subtable = table.getSubtable(1, ROW);
        TableSpec subspecMixed = subtable.getTableSpec();
        subspecMixed.addColumn(RealmFieldType.INTEGER, "num");
        subtable.updateFromSpec(subspecMixed);

        // Insert value in the Mixed subtable
        subtable.add(27);
        subtable.add(273);
        assertEquals(2, subtable.size());
        assertEquals(2, table.getSubtableSize(1, ROW));
        assertEquals(27, subtable.getLong(0, ROW));
        assertEquals(273, subtable.getLong(0, ROW+1));
        */
    }

    /* FIXME: Subtable in Mixed is currently not supported
    public void testShouldCreateSubtableInMixedTypeColumn2() {
        Table table = new Table();

        TableSpec tableSpec = new TableSpec();
        tableSpec.addColumn(RealmFieldType.INTEGER, "num");
        tableSpec.addColumn(RealmFieldType.UNSUPPORTED_MIXED, "mix");
        table.updateFromSpec(tableSpec);

        table.addEmptyRow();
        table.setMixed(1, 0, new Mixed(RealmFieldType.UNSUPPORTED_TABLE));

        Table subtable = table.getSubtable(1, 0);
    }
    */
}
