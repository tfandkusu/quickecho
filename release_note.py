import os
from github import Github


class Repository:
    "リポジトリからの情報出し入れ担当"

    def __init__(self, repository):
        self.repo = repository

    def make_marged_prs(self):
        "masterブランチにマージ済みのプルリク一覧テキストを作成する"
        tag = self.get_latest_release_tag()
        if(tag is None):
            raise RuntimeError("最新のreleaseタグがありません。")
        commits = self.get_commits(tag.commit.commit.sha)
        numbers = self.get_marged_pr_numbers(commits)
        prs = self.get_prs_in_numbers(numbers)
        reverts = self.get_revert_pr_numbers(commits)
        rprs = self.get_prs_in_numbers(reverts)
        # テキストを作成する
        text = ""
        for pr in prs:
            text += "- %s #%d\n" % (pr.title, pr.number)
        for pr in rprs:
            text += "- Revert %s #%d\n" % (pr.title, pr.number)
        return text

    def create_release(self, tag_name, version_name, text):
        "リリースノートを作成する"
        self.repo.create_git_release(tag_name, version_name, text)

    def get_latest_release_tag(self):
        "一番新しいリリースタグを取得する"
        tags = self.repo.get_tags()
        latest_tag = None
        for tag in tags:
            if(tag.name.startswith("release_")):
                latest_tag = tag
                break
        return latest_tag

    def get_commits(self, sha):
        "指定したハッシュまでのコミット一覧を取得する"
        # masterブランチのコミット一覧を取得
        results = []
        commits = self.repo.get_commits()
        for commit in commits:
            gc = commit.commit
            if(gc.sha == sha):
                break
            results.append(gc)
        return results

    def get_marged_pr_numbers(self, commits):
        "コミット一覧からマージされたプルリク番号を得る"
        numbers = []
        for commit in commits:
            message = commit.message
            if(message.startswith("Merge pull request #")):
                items = message.split()
                number = int(items[3][1:])
                numbers.append(number)
        return numbers

    def get_revert_pr_numbers(self, commits):
        "コミット一覧からリバートされたプルリク番号を得る"
        numbers = []
        for commit in commits:
            message = commit.message
            if(message.startswith("Revert \"Merge pull request #")):
                items = message.split()
                number = int(items[4][1:])
                numbers.append(number)
        return numbers

    def get_prs_in_numbers(self, numbers):
        "プルリク番号からプルリク一覧にする"
        prs = []
        for number in numbers:
            pr = self.repo.get_pull(number)
            prs.append(pr)
        return prs


def get_version_and_tag_name():
    "バージョン名とタグ名を作る"
    major = 1
    minor = 0
    patch = 0
    with open("build.gradle") as f:
        for line in f.readlines():
            if(line.startswith("    ext.major = ")):
                major = int(line.split()[-1])
            if(line.startswith("    ext.minor = ")):
                minor = int(line.split()[-1])
            if(line.startswith("    ext.patch = ")):
                patch = int(line.split()[-1])
    version_name = "%d.%d.%d" % (major, minor, patch)
    tag_name = "release_%d_%d_%d" % (major, minor, patch)
    return version_name, tag_name


# バージョン名とタグ名を作る
version_name, tag_name = get_version_and_tag_name()
# アクセストークンを持ってGithubオブジェクトを作成
access_token = os.environ['BITRISEIO_GITHUB_API_ACCESS_TOKEN']
g = Github(access_token)
# リポジトリを取得
gr = g.get_user().get_repo("quickecho")
# Organizationを使っている場合
# gr = g.get_organization("organization_name").get_repo("repository_name")
# インスタンスにする
r = Repository(gr)
# masterブランチにマージ済みプルリク一覧テキストを作成する
text = r.make_marged_prs()
# デバッグ表示する
print("%s %s" % (version_name, tag_name))
print("")
print(text)
# git releaseを作成する
r.create_release(tag_name, version_name, text)
