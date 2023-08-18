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
        "start-address": {
            type: "string"
        },
        "end-address": {
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

delete xmlObject.Profile.loginIpRanges;

const newLoginIpRanges = {
    endAddress: parsed.values['end-address'],
    startAddress: parsed.values['start-address']
};
xmlObject.Profile['loginIpRanges'] = newLoginIpRanges;

const distXml = new XMLBuilder(options).build(xmlObject);
const distPath = parsed.values.dst;
fs.writeFileSync(distPath, distXml, 'utf8')
