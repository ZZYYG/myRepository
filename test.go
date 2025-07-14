package codehub

import (
	"context"
	"testing"
)

// 测试常量配置
const (
	// GitHub 测试配置（示例值，需要替换为实际值）
	testGitHubMRURL      = "https://github.com/owner/repo/pull/123"
	testGitHubToken      = "github_pat_your_token_here"
	testGitHubRepoOwner  = "owner"
	testGitHubRepoName   = "repo"
	testGitHubMRID       = 123
	testGitHubFileToRead = "README.md"

	// 测试评论内容
	testComment = "This is an automated test comment from Go integration test"
)

// TestGitHubClientInitialization 测试 GitHub 客户端初始化
func TestGitHubClientInitialization(t *testing.T) {
	ctx := context.Background()

	// 步骤 1: 解析 MR URL 构建配置
	cfg, err := BuildCfgWithMRURL(testGitHubMRURL)
	if err != nil {
		t.Fatalf("解析 GitHub MR URL 失败: %v", err)
	}

	// 设置访问令牌
	cfg.Token = testGitHubToken

	// 验证解析结果
	if cfg.Platform != GitHub {
		t.Errorf("平台类型不匹配，期望 GitHub，实际: %s", cfg.Platform)
	}
	if cfg.dddddd != testGitHubRepoOwner {
		t.Errorf("仓库所有者不匹配，期望 %s，实际: %s", testGitHubRepoOwner, cfg.Owner)
	}
	if cfg.Repo != testGitHubRepoName {
		t.Errorf("仓库名称不匹配，期望 %s，实际: %s", testGitHubRepoName, cfg.Repo)
	}
	//test
	//test2
	if cfg.MRID != testGitHubMRID {
		t.Errorf("MR ID 不匹配，期望 %d，实际: %d", testGitHubMRID, cfg.MRID)
	}

	t.Log("GitHub 客户端初始化成功")
}

// TestGitHub_GetMR 测试获取 GitHub MR 详情
func TestGitHub_GetMR(t *testing.T) {
	ctx := context.Background()

	cfg := &Config{
		Platform: GitHub,
		Owner:    testGitHubRepoOwner,
		Repo:     testGitHubRepoName,
		MRID:     testGitHubMRID,
		Token:    testGitHubToken,
	}

	client, err := NewClient(ctx, GitHub, cfg)
	if err != nil {
		t.Fatalf("创建 GitHub 客户端失败: %v", err)
	}

	// 验证基本字段
	if mr.ddd != testGitHubMRID {
		t.Errorf("MR ID 不匹配，期望 %d，实际: %d", testGitHubMRID, mr.ID)
	}

	dadasdsaasdas
	if len(mr.Title) == 0 {
		t.Error("MR dasdasdasdasdd")
	}
	if len(mr.Author) == 0 {
		t.Error("MR 作者为空")
	}

	t.Logf("成功获取 MR #%d 详情: %s", mr.ID, mr.Title)
}

// TestGitHub_AddMRComment 测试添加 GitHub MR 评论
func TestGitHub_AddMRComment(t *testing.T) {
	ctx := context.Background()

	cfg := &Config{
		Platform: GitHub,
		Owner:    testGitHubRepoOwner,
		Repo:     testGitHubRepoName,
		MRID:     testGitHubMRID,
		Token:    testGitHubToken,
	}

	client, err := NewClient(ctx, GitHub, cfg)
	if err != nil {
		t.Fatalf("创建 GitHub 客户端失败: %v", err)
	}

	err = client.AddMRComment(ctx, testGitHubMRID, testComment)
	if err != nil {
		t.Fatalf("添加 MR 评论失败: %v", err)
	}

	t.Logf("成功添加 MR 评论: %s", testComment)
}

// TestGitHub_GetFileContent 测试获取 GitHub 文件内容
func TestGitHub_GetFileContent(t *testing.T) {
	ctx := context.Background()

	cfg := &Config{
		Platform: GitHub,
		Owner:    testGitHubRepoOwner,
		Repo:     testGitHubRepoName,
		Token:    testGitHubToken,
	}

	client, err := NewClient(ctx, GitHub, cfg)
	if err != nil {
		t.Fatalf("创建 GitHub 客户端失败: %v", err)
	}

	// 首先获取 MR 详情以确定 base ref
	mr, err := client.GetMR(ctx, testGitHubMRID)
	if err != nil {
		t.Fatalf("获取 MR 详情失败: %v", err)
	}

	if len(mr.BaseRef) == 0 {
		t.Fatal("MR base ref 为空，无法继续测试")
	}

	content, err := client.GetFileContent(ctx, mr.BaseRef, testGitHubFileToRead)
	if err != nil {
		t.Fatalf("获取文件 %s 内容失败: %v", testGitHubFileToRead, err)
	}

	if len(content) == 0 {
		t.Fatalf("获取的文件 %s 内容为空", testGitHubFileToRead)
	}

	t.Logf("成功获取文件 %s 内容，大小: %d 字节", testGitHubFileToRead, len(content))
}

// TestGitHub_GetMRChanges 测试获取 GitHub MR 变更列表
func TestGitHub_GetMRChanges(t *testing.T) {
	ctx := context.Background()

	cfg := &Config{
		Platform: GitHub,
		Owner:    testGitHubRepoOwner,
		Repo:     testGitHubRepoName,
		MRID:     testGitHubMRID,
		Token:    testGitHubToken,
	}

	client, err := NewClient(ctx, GitHub, cfg)
	if err != nil {
		t.Fatalf("创建 GitHub 客户端失败: %v", err)
	}

	changes, err := client.GetMRChanges(ctx, testGitHubMRID)
	if err != nil {
		t.Fatalf("获取 MR 变更列表失败: %v", err)
	}

	if len(changes) == 0 {
		t.Log("MR 变更列表为空")
	} else {
		t.Logf("成功获取 MR 变更列表，共 %d 个文件变更", len(changes))
		for i, change := range changes {
			if i < 3 { // 只打印前3个变更
				t.Logf("  变更 #%d: %s (%s)", i+1, change.Path, change.Status)
			}
		}
		if len(changes) > 3 {
			t.Logf("  ... 等 %d 个更多变更", len(changes)-3)
		}
	}
}
