package main.resources;

import java.io.*;
import java.util.*;


public class TranslationCheck {

    private static class OrderedProperties extends Properties {

        private Vector _names;

        public OrderedProperties() {
            super ();

            _names = new Vector();
        }

        public Enumeration propertyNames() {
            return _names.elements();
        }

        public Object put(Object key, Object value) {
            if (_names.contains(key)) {
                _names.remove(key);
            }

            _names.add(key);

            return super .put(key, value);
        }

        public Object remove(Object key) {
            _names.remove(key);

            return super .remove(key);
        }

    }

    private static Properties load_properties_file(String file_name, String relative_path)
            throws IOException {

        Properties props_file = new OrderedProperties();
        String file_absolute_path = new File(relative_path.concat(file_name)).getAbsolutePath();

        InputStream input = new FileInputStream(file_absolute_path);
        props_file.load(input);

        return props_file;

    }

    private static Vector<String> get_file_property_keys(String file_name, String relative_path)
            throws IOException {

        Properties props = load_properties_file(file_name, relative_path);

        Vector<String> file_props = new Vector();
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            file_props.add(key);
        }

        return file_props;
    }

    private static void check_files_are_sorted(ArrayList<String> file_name_list, String relative_path)
            throws IOException {

        // check if translation files are sorted alphabetically
        for (String file_name : file_name_list) {
            Vector<String> props_keys = get_file_property_keys(file_name, relative_path);

            // empty string: guaranteed to be less than or equal to any other
            String previous_key = "";
            for (String current_key : props_keys) {
                if (current_key.compareTo(previous_key) < 0) {
                    System.out.println("Failed: " + file_name + " file is not sorted alphabetically");
                    break;
                }
                previous_key = current_key;
            }
        }
    }

    private static ArrayList<String> get_file_name_list(String path, String extension)
            throws Exception {

        ArrayList<String> file_name_list = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();

        for (File f : files) {
            if (f.getName().endsWith(extension)) {
                file_name_list.add(f.getName());
            }
        }

        if (file_name_list.isEmpty()) {
            throw new Exception("Failed: No translation files were found");
        } else {
            return file_name_list;
        }

    }

    private static String get_default_file(ArrayList<String> file_name_list)
            throws Exception {

        for (String file_name : file_name_list) {
            if (file_name.contains("_de."))
                return file_name;
        }
        throw new Exception("Failed: No default translation file was found to compare with");
    }

    private static void check_files_are_equal(ArrayList<String> file_name_list, String relative_path)
            throws Exception {

        // get default file to compare with
        String default_file = get_default_file(file_name_list);
        // read default file properties
        Vector default_file_props = get_file_property_keys(default_file, relative_path);
        Collections.sort(default_file_props);  // sort alphabetically to allow comparison to other files

        for (String file_name : file_name_list) {
            Vector file_props = get_file_property_keys(file_name, relative_path);
            Collections.sort(file_props);
            boolean props_are_equal = default_file_props.equals(file_props);
            if (!props_are_equal)
                System.out.println("Failed: " + file_name + " properties are not equal to "
                        + default_file + " properties");
        }

    }

    private static void check_file_properties_are_not_empty(ArrayList<String> file_name_list, String relative_path)
            throws IOException {

        for (String file_name : file_name_list) {

            Properties props_file = load_properties_file(file_name, relative_path);

            for (Enumeration e = props_file.propertyNames(); e.hasMoreElements(); ) {
                String prop_key = (String) e.nextElement();
                if (props_file.getProperty(prop_key).isEmpty()) {
                    System.out.println("Failed: " + file_name + " properties are empty for the "
                            + prop_key + " property");
                }

            }
        }
    }

    private static void run_translations_check(String relative_path) {

        String dir_absolute_path = new File(relative_path).getAbsolutePath();

        ArrayList<String> file_name_list = null;
        try {
            // get translation files
            file_name_list = get_file_name_list(dir_absolute_path, ".properties");
            // check if translation files are sorted
            check_files_are_sorted(file_name_list, relative_path);
            // check if files have the same properties
            check_files_are_equal(file_name_list, relative_path);
            // check if file properties are not empty
            check_file_properties_are_not_empty(file_name_list, relative_path);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        // backend relative path
        String relative_path = "src/main/resources/";
        System.out.println("-- Start checking backend translations --");
        run_translations_check(relative_path);
        System.out.println("-- End checking Backend translations --");

        // keycloak relative path for email
        relative_path = "src/main/resources/themes/caseform-email/email/messages/";
        System.out.println("-- Start checking Keycloak email translations --");
        run_translations_check(relative_path);
        System.out.println("-- End checking Keycloak email translations --");

        // keycloak relative path for messages
        relative_path = "src/main/resources/themes/caseform-login/login/messages/";
        System.out.println("-- Start checking Keycloak login translations --");
        run_translations_check(relative_path);
        System.out.println("-- End checking Keycloak login translations --");


    }
}



