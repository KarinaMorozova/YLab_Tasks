package my.ylab.homework03.structure;

import java.io.File;
import java.io.IOException;

public class OrgStructureParserTest {
    public static void main(String[] args) {
        OrgStructureParserImpl parser = new OrgStructureParserImpl();

        try {
            File file = new File("./src/main/java/my/ylab/homework03/structure/resources/data.csv");
            Employee boss = parser.parseStructure(file);

            System.out.println("Начальник: " +  boss.getName());
            System.out.println("Подчиненные: " +  boss.getSubordinate());

            System.out.println();
            System.out.println("Работники, у которых есть подчиненные: ");
            for (Employee e: boss.getSubordinate()) {
                if (e.getSubordinate().size() != 0) {
                    System.out.println(e.getName() + " " + e.getSubordinate());
                }
            }
        }
        catch (IOException ex) {
            ex.getStackTrace();
        }

    }
}
