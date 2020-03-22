module.exports = {
    extends: "recommended",
    rules: {
        "comments.ClassJavadoc": 'off',
        "formatting.Indentation": {
            spacesPerIndentLevel: 4,
            severity: "info"
        },
        "ignorepattern": "**/target/*",
        'UnnecessaryReturnKeyword': "error"
    }
}
