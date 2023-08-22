const fs = require('fs');
const util = require('util');
const { XMLParser, XMLBuilder } = require('fast-xml-parser');

const parsed = util.parseArgs({
    options: {
        src: {
            type: "string"
        },
        dst: {
            type: "string"
        },
        "post-install-class": {
            type: "string"
        },
        "uninstall-class": {
            type: "string"
        }
    }
});

const options = {
    ignoreAttributes: false,
    format: true,
    indentBy: '    ',
};

const parser = new XMLParser(options);

const srcPath = parsed.values.src;
const xml = fs.readFileSync(srcPath, 'utf-8').toString();
const xmlObject = parser.parse(xml);

delete xmlObject.Package.postInstallClass;
delete xmlObject.Package.uninstallClass;

const postInstallClass = parsed.values['post-install-class'];
if (postInstallClass) {
    xmlObject.Package['postInstallClass'] = postInstallClass;
}

const uninstallClass = parsed.values['uninstall-class'];
if (uninstallClass) {
    xmlObject.Package['uninstallClass'] = uninstallClass;
}

const distXml = new XMLBuilder(options).build(xmlObject);
const distPath = parsed.values.dst;
fs.writeFileSync(distPath, distXml, 'utf8')
