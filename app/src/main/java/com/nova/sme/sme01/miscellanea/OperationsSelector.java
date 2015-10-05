package com.nova.sme.sme01.miscellanea;

import com.nova.sme.sme01.xml_reader_classes.Operation;

import java.io.Serializable;
import java.util.Vector;

/*
 ***************************************
 *                                     *
 *   Class saves selected operations   *
 *   and assists efficient filtering   *
 *                                     *
 ***************************************
 */
public class OperationsSelector implements Serializable {
    private Vector<ShortOperation> operations                = new Vector<ShortOperation>();
    private Vector<String>         checkedOperationsFullName = new Vector<String>();
    private Vector<String>         checkedOperations         = new Vector<String>();

    public OperationsSelector() {

    }

    Vector<ShortOperation> getOperations(){return operations;}

    public boolean isEmpty() {
        initChecked();
        return (checkedOperations.size() == 0);
    }
/*
    public ShortOperation get(int index) {
        return operations.get(index);
    }
*/
    public void addOperation(Operation operation) { // used during initialization
        String operationType = operation.getName().trim();

        ShortOperation sh_op = new ShortOperation();
        sh_op.checked   = true;
        sh_op.name      = operationType;

        if (operation.getInbound().equals("true")) {
            sh_op.in_out    = "IN";
            sh_op.full_name = "IN: " + operationType;
        } else {
            sh_op.in_out    = "OUT";
            sh_op.full_name = "OUT: " + operationType;
        }
        operations.add(sh_op);
        checkedOperations.add(operationType);
    }

    public boolean isChecked(String value) {
        if (checkedOperations.size() == 0) return true; // first time
        return checkedOperations.contains(value);
    }
    public boolean isCheckedFullName(String value) {
        if (checkedOperationsFullName.size() == 0) return true; // first time
        return checkedOperationsFullName.contains(value);
    }//OUT:Bank Charges
     //OUT: Bank Charges  OUT: Bank Charges
     //IN: Capital Injection
    public void initChecked() {
        checkedOperations.clear();
        checkedOperationsFullName.clear();
        for (int j = 0; j < operations.size(); j ++) {
            if (operations.get(j).checked) {
                checkedOperationsFullName.add(operations.get(j).full_name);
                checkedOperations.add(operations.get(j).name);
            }
        }
    }
}
