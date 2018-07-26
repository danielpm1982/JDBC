package jdbc;
public class Department {
    private int departmentNo;
    private String nameDep;
    public Department() {
        this(-1,null);
    }
    public Department(int departmentNo, String nameDep) {
        this.departmentNo = departmentNo;
        this.nameDep = nameDep;
    }
    public void setDepartmentNo(int departmentNo) {
        this.departmentNo = departmentNo;
    }
    public int getDepartmentNo() {
        return departmentNo;
    }
    public void setNameDep(String nameDep) {
        this.nameDep = nameDep;
    }
    public String getNameDep() {
        return nameDep;
    }
    @Override
    public String toString() {
        return departmentNo+" "+nameDep;
    }
}
