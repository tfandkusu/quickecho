# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Android Lint
android_lint.skip_gradle_task = true
android_lint.filtering = true
Dir["*/build/reports/lint-results-debug.xml"].each do |file|
  android_lint.report_file = file
  android_lint.lint(inline_mode: false)
end
