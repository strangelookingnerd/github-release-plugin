import glob
import json
import os.path
import os
from os.path import dirname


for mapping_file in glob.glob('./src/test/resources/org/jenkinsci/plugins/github/release/**/mappings.json', recursive=True):
    mapping_data = None
    with open(mapping_file, 'r') as f:
        mapping_data = json.loads(f.read())

    test_directory = os.path.dirname(mapping_file)
    if not os.path.exists(test_directory):
        os.mkdir(test_directory)
    mapping_directory = os.path.join(test_directory, 'mappings')
    body_directory = os.path.join(test_directory, '__files')

    if not os.path.exists(mapping_directory):
        os.mkdir(mapping_directory)
    if not os.path.exists(body_directory):
        os.mkdir(body_directory)

    for mapping in mapping_data['mappings']:
        name = mapping['name']
        mapping_name = "mapping-{}.json".format(name)
        body_name = "body-{}.json".format(name)
        mapping_output_file = os.path.join(mapping_directory, mapping_name)
        body_output_file = os.path.join(body_directory, body_name)

        with open(mapping_output_file, 'w') as f:
            f.write(json.dumps(mapping, indent=2))

        body = mapping['response']['body']
        with open(body_output_file, 'w') as f:
            t = json.loads(body)
            f.write(json.dumps(t, indent=2))





        print(name)
        print(mapping_file)


