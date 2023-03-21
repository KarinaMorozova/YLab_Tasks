package my.ylab.homework03.structure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OrgStructureParserImpl implements OrgStructureParser{
    Map<Long, Employee> totalMap = new HashMap<>();

    private Employee createEmployee(String[] record) {
        Employee employee = new Employee();

        try {
            employee.setId(Long.valueOf(record[0]));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        employee.setBossId( (!record[1].isEmpty()) ? Long.valueOf(record[1]) : null );
        employee.setName(record[2]);
        employee.setPosition(record[3]);

        return employee;
    }

    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        Map<Long, Long> connMap = new HashMap<>();
        Employee boss = null;

        try (FileReader fileReader = new FileReader(csvFile);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String stringFromFile;
            boolean firstLine = true;

            while (( stringFromFile = reader.readLine()) != null ) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] record = stringFromFile.split(";");
                Employee current = createEmployee(record);

                totalMap.putIfAbsent(current.getId(), current);
                connMap.putIfAbsent(current.getId(), current.getBossId());

                if (current.getBossId() == null) {
                    boss = current;
                }
            }

            for (Map.Entry<Long, Long> entry: connMap.entrySet()) {
                Long bossId = entry.getValue();
                if ( totalMap.containsKey(bossId) ) {
                    Employee employee = totalMap.get(entry.getKey());
                    totalMap.get(bossId).getSubordinate().add(employee);
                }

                Employee tempBoss = totalMap.get(bossId);
                totalMap.get(entry.getKey()).setBoss(tempBoss);
            }
        }

        return boss;
    }
}
