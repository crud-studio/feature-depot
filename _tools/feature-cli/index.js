const commander = require("commander");
const capitalize = require('capitalize');
const changeCase = require('change-case');
const path = require('path');
const fs = require('fs-extra');
const Mustache = require('mustache');
const program = new commander.Command();

const featureCommand = program.command('feature');

featureCommand
    .command('create')
    .argument('<feature name>', "the name of the feature")
    .action(createFeature);



async function createFeature(featureName) {
    const featureNameCapitalized = changeCase.pascalCase(featureName);
    const featurePackageName = featureName.replace('-', '');
    const context = {featureName, featureNameCapitalized, featurePackageName};
    
    fs.copySync('./_tools/skeleton', featureName)
    const fileNames = getFilesRecursively(featureName);
    for(let fileName of fileNames) {
        const compiledFileContent = Mustache.render(fs.readFileSync(fileName, 'utf-8'), context);
        fs.writeFileSync(fileName, compiledFileContent)
        const compiledFileName = Mustache.render(fileName, context);
        if(fileName !== compiledFileName) {
            fs.moveSync(fileName, compiledFileName)
        }
    }
    fs.moveSync(
        path.join(featureName, 'src', 'main', 'java', 'studio', 'crud', 'feature', 'skeleton'),
        path.join(featureName, 'src', 'main', 'java', 'studio', 'crud', 'feature', featurePackageName)
    )

    addModuleToParentPom(featureName)
    
}

function addModuleToParentPom(featureName) {
    const moduleToAdd = `\n\t\t<module>${featureName}</module>\n\t</modules>`
    const content = fs.readFileSync('pom.xml', 'utf-8');
    console.log('Adding feature to pom');
    const replacedContent = content.replace("\n\t</modules>", moduleToAdd)
    fs.writeFileSync('pom.xml', replacedContent)
}

function getFilesRecursively(basePath) {
    let current = fs.readdirSync(basePath).map(item => {
        const stat = fs.statSync(path.join(basePath, item))
        if(stat.isDirectory()) {
            return [
                ...getFilesRecursively(path.join(basePath, item)).flat()
            ]
        } else {
            return path.join(basePath, item);
        }
    }).flat();
    return current;
}

program.parse(process.argv)
